package com.achhecode.SpringBot.automation.zip;

import com.achhecode.SpringBot.exception.ZipCommandExecutionException;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.event.KeyEvent;
import java.util.List;

@Slf4j
@Component
public class FastZipCommandExecutor implements ZipCommandExecutor {

    public interface CoreGraphics extends Library {
        CoreGraphics INSTANCE = Native.load("CoreGraphics", CoreGraphics.class);

        Pointer CGEventSourceCreate(int stateID);
        Pointer CGEventCreateKeyboardEvent(Pointer source, short virtualKey, boolean keyDown);
        void CGEventPost(int tap, Pointer event);
        void CFRelease(Pointer obj);
    }

    private static final int K_CG_EVENT_SOURCE_STATE_COMBINED_SESSION_STATE = 0;
    private static final int K_CG_HID_EVENT_TAP = 0;

    // macOS virtual keycodes (NOT java.awt.event.KeyEvent codes)
    private static final short MAC_VK_COMMAND = 0x37;
    private static final short MAC_VK_TAB = 0x30;
    private static final short MAC_VK_UP_ARROW = 0x7E;
    private static final short MAC_VK_DOWN_ARROW = 0x7D;
    private static final short MAC_VK_LEFT_ARROW = 0x7B;
    private static final short MAC_VK_RIGHT_ARROW = 0x7C;

    @Value("${automation.keyboard.switch-application:true}")
    private boolean switchApplication;

    /**
     * Delay in milliseconds after each arrow command, so the target
     * application has time to actually process the keystroke before the
     * next one arrives. Override via application.properties/yml or an
     * env var, e.g.:
     *   automation.keyboard.command-delay-ms=20
     */
    @Value("${automation.keyboard.command-delay-ms:20}")
    private long commandDelayMs;

    /**
     * Delay in milliseconds after the Cmd+Tab application switch, before
     * the first arrow command is sent — gives the target app time to
     * actually gain focus. Override via:
     *   automation.keyboard.post-switch-delay-ms=200
     */
    @Value("${automation.keyboard.post-switch-delay-ms:200}")
    private long postSwitchDelayMs;

    @Value("${automation.keyboard.combo-delay-ms:50}")
    private long comboDelayMs;

    private Pointer eventSource;

    @PostConstruct
    public void initialize() {
        eventSource = CoreGraphics.INSTANCE.CGEventSourceCreate(
            K_CG_EVENT_SOURCE_STATE_COMBINED_SESSION_STATE
        );
        log.info("CGEventSource initialized for fast keyboard automation");
    }

    @Override
    public synchronized void execute(List<ZipCommand> commands, String executionId) {
        if (commands == null || commands.isEmpty()) {
            log.warn("No keyboard commands to execute. executionId={}", executionId);
            return;
        }

        long totalStart = System.nanoTime();

        try {
            if (switchApplication) {
                postCombo(MAC_VK_COMMAND, MAC_VK_TAB);
                if (postSwitchDelayMs > 0) {
                    sleep(postSwitchDelayMs);
                }
            }

            for (int i = 0; i < commands.size(); i++) {
                short macKey = keyCodeToMacVirtualKey(commands.get(i).getKeyCode());
                postKey(macKey);

                boolean isLastCommand = i == commands.size() - 1;
                if (commandDelayMs > 0 && !isLastCommand) {
                    sleep(commandDelayMs);
                }
            }

            long totalMs = (System.nanoTime() - totalStart) / 1_000_000;
            log.info(
                "Fast keyboard automation completed. executionId={}, commandCount={}, totalMs={}",
                executionId, commands.size(), totalMs
            );

        } catch (Exception e) {
            log.error("Fast keyboard automation failed. executionId={}", executionId, e);
            throw new ZipCommandExecutionException("Keyboard automation failed", executionId, e);
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ZipCommandExecutionException(
                "Keyboard automation interrupted during inter-command delay",
                "n/a",
                e
            );
        }
    }

    private short keyCodeToMacVirtualKey(int javaVkCode) {
        return switch (javaVkCode) {
            case KeyEvent.VK_UP -> MAC_VK_UP_ARROW;
            case KeyEvent.VK_DOWN -> MAC_VK_DOWN_ARROW;
            case KeyEvent.VK_LEFT -> MAC_VK_LEFT_ARROW;
            case KeyEvent.VK_RIGHT -> MAC_VK_RIGHT_ARROW;
            default -> throw new IllegalArgumentException(
                "No macOS virtual keycode mapping for java VK code: " + javaVkCode
            );
        };
    }

    private void postKey(short macVirtualKey) {
        Pointer down = CoreGraphics.INSTANCE.CGEventCreateKeyboardEvent(eventSource, macVirtualKey, true);
        CoreGraphics.INSTANCE.CGEventPost(K_CG_HID_EVENT_TAP, down);
        CoreGraphics.INSTANCE.CFRelease(down);

        Pointer up = CoreGraphics.INSTANCE.CGEventCreateKeyboardEvent(eventSource, macVirtualKey, false);
        CoreGraphics.INSTANCE.CGEventPost(K_CG_HID_EVENT_TAP, up);
        CoreGraphics.INSTANCE.CFRelease(up);
    }

    private void postCombo(short modifierKey, short key) {
        Pointer modDown = CoreGraphics.INSTANCE.CGEventCreateKeyboardEvent(
            eventSource, modifierKey, true
        );
        CoreGraphics.INSTANCE.CGEventPost(K_CG_HID_EVENT_TAP, modDown);
        CoreGraphics.INSTANCE.CFRelease(modDown);

        sleep(comboDelayMs);

        Pointer keyDown = CoreGraphics.INSTANCE.CGEventCreateKeyboardEvent(
            eventSource, key, true
        );
        CoreGraphics.INSTANCE.CGEventPost(K_CG_HID_EVENT_TAP, keyDown);
        CoreGraphics.INSTANCE.CFRelease(keyDown);

        sleep(comboDelayMs);

        Pointer keyUp = CoreGraphics.INSTANCE.CGEventCreateKeyboardEvent(
            eventSource, key, false
        );
        CoreGraphics.INSTANCE.CGEventPost(K_CG_HID_EVENT_TAP, keyUp);
        CoreGraphics.INSTANCE.CFRelease(keyUp);

        sleep(comboDelayMs);

        Pointer modUp = CoreGraphics.INSTANCE.CGEventCreateKeyboardEvent(
            eventSource, modifierKey, false
        );
        CoreGraphics.INSTANCE.CGEventPost(K_CG_HID_EVENT_TAP, modUp);
        CoreGraphics.INSTANCE.CFRelease(modUp);
    }

    
}