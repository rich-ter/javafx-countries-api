package com.richter.CountriesAppJFX;

import java.util.ArrayList;
import java.util.List;

public class SearchHistory {
    private final List<String> history = new ArrayList<>(5);

    public void addSearch(String search) {
        if (history.contains(search)) {
            history.remove(search);
        } else if (history.size() == 5) {
            history.remove(0); // Remove the oldest search
        }
        history.add(search); // Add the new search at the end
    }

    public List<String> getHistory() {
        return new ArrayList<>(history); // Return a copy to prevent modification
    }
}