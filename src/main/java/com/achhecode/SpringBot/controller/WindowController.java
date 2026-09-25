package com.achhecode.SpringBot.controller;

import com.achhecode.SpringBot.automation.window.WindowCommand;
import com.achhecode.SpringBot.automation.window.WindowExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/automation/window")
public class WindowController {

    private final WindowExecutor windowExecutor;

    public WindowController(WindowExecutor windowExecutor) {
        this.windowExecutor = windowExecutor;
    }

    // POST /api/automation/window/next

    @PostMapping("/next")
    public String nextApplication() {

        windowExecutor.execute(
                WindowCommand.NEXT_APPLICATION
        );

        return "Switched to next application";
    }
}