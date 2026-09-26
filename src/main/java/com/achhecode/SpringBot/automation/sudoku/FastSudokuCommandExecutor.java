package com.achhecode.SpringBot.automation.sudoku;

import com.achhecode.SpringBot.exception.SudokuCommandExecutionException;
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
public class FastSudokuCommandExecutor implements SudokuCommandExecutor {

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

    private static final short MAC_VK_0 = 0x1D;
    private static final short MAC_VK_1 = 0x12;
    private static final short MAC_VK_2 = 0x13;
    private static final short MAC_VK_3 = 0x14;
    private static final short MAC_VK_4 = 0x15;
    private static final short MAC_VK_5 = 0x17;
    private static final short MAC_VK_6 = 0x16;
    private static final short MAC_VK_7 = 0x1A;
    private static final short MAC_VK_8 = 0x1C;
    private static final short MAC_VK_9 = 0x19;

    private static final short MAC_VK_DELETE = 0x33;        // backspace
    private static final short MAC_VK_FORWARD_DELETE = 0x75; // delete
    private static final short MAC_VK_RETURN = 0x24;         // enter
    private static final short MAC_VK_SPACE = 0x31;

    @Value("${automation.keyboard.switch-application:true}")
    private boolean switchApplication;

    /**
     * Delay in milliseconds after each Sudoku command (digit/arrow/etc),
     * so the target app has time to process it. Override via:
     *   automation.keyboard.sudoku.command-delay-ms=20
     */
    @Value("${automation.keyboard.sudoku.command-delay-ms:20}")
    private long commandDelayMs;

    /**
     * Delay in milliseconds after the Cmd+Tab application switch, before
     * the first command is sent. Override via:
     *   automation.keyboard.sudoku.post-switch-delay-ms=200
     */
    @Value("${automation.keyboard.sudoku.post-switch-delay-ms:200}")
    private long postSwitchDelayMs;

    private Pointer eventSource;

    @PostConstruct
    public void initialize() {
        eventSource = CoreGraphics.INSTANCE.CGEventSourceCreate(
            K_CG_EVENT_SOURCE_STATE_COMBINED_SESSION_STATE
        );
        log.info("CGEventSource initialized for fast Sudoku keyboard automation");
    }

    @Override
    public void execute(List<SudokuInputCommand> commands, String executionId) {

        log.info(
            "Sudoku keyboard automation started. executionId={}, commandCount={}, switchApplication={}",
            executionId, commands.size(), switchApplication
        );

        long start = System.nanoTime();

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

            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            log.info(
                "Sudoku commands executed. executionId={}, count={}, elapsedMs={}",
                executionId, commands.size(), elapsedMs
            );

        } catch (Exception e) {
            log.error("Sudoku keyboard automation failed. executionId={}", executionId, e);
            throw new SudokuCommandExecutionException(
                "Sudoku keyboard automation failed",
                executionId,
                e
            );
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SudokuCommandExecutionException(
                "Sudoku keyboard automation interrupted during inter-command delay",
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
            case KeyEvent.VK_0, KeyEvent.VK_NUMPAD0 -> MAC_VK_0;
            case KeyEvent.VK_1, KeyEvent.VK_NUMPAD1 -> MAC_VK_1;
            case KeyEvent.VK_2, KeyEvent.VK_NUMPAD2 -> MAC_VK_2;
            case KeyEvent.VK_3, KeyEvent.VK_NUMPAD3 -> MAC_VK_3;
            case KeyEvent.VK_4, KeyEvent.VK_NUMPAD4 -> MAC_VK_4;
            case KeyEvent.VK_5, KeyEvent.VK_NUMPAD5 -> MAC_VK_5;
            case KeyEvent.VK_6, KeyEvent.VK_NUMPAD6 -> MAC_VK_6;
            case KeyEvent.VK_7, KeyEvent.VK_NUMPAD7 -> MAC_VK_7;
            case KeyEvent.VK_8, KeyEvent.VK_NUMPAD8 -> MAC_VK_8;
            case KeyEvent.VK_9, KeyEvent.VK_NUMPAD9 -> MAC_VK_9;
            case KeyEvent.VK_BACK_SPACE -> MAC_VK_DELETE;
            case KeyEvent.VK_DELETE -> MAC_VK_FORWARD_DELETE;
            case KeyEvent.VK_ENTER -> MAC_VK_RETURN;
            case KeyEvent.VK_SPACE -> MAC_VK_SPACE;
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
        Pointer modDown = CoreGraphics.INSTANCE.CGEventCreateKeyboardEvent(eventSource, modifierKey, true);
        CoreGraphics.INSTANCE.CGEventPost(K_CG_HID_EVENT_TAP, modDown);
        CoreGraphics.INSTANCE.CFRelease(modDown);

        postKey(key);

        Pointer modUp = CoreGraphics.INSTANCE.CGEventCreateKeyboardEvent(eventSource, modifierKey, false);
        CoreGraphics.INSTANCE.CGEventPost(K_CG_HID_EVENT_TAP, modUp);
        CoreGraphics.INSTANCE.CFRelease(modUp);
    }
}