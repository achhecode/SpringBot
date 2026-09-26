package com.achhecode.SpringBot.automation.nqueen;

import java.util.List;

public record NQueenRequest(
        List<Integer> positions
) {
}