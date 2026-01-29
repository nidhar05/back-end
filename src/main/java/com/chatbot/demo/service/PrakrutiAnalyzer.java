package com.chatbot.demo.service;

public class PrakrutiAnalyzer {

    public static boolean hasSymptoms(String text) {
        text = text.toLowerCase();
        return text.contains("pain") ||
                text.contains("burning") ||
                text.contains("heat") ||
                text.contains("swelling") ||
                text.contains("dry") ||
                text.contains("stiff") ||
                text.contains("heavy") ||
                text.contains("joint") ||
                text.contains("muscle");
    }

    public static String assess(String text) {

        text = text.toLowerCase();

        int vata = 0, pitta = 0, kapha = 0;

        if (text.contains("dry") || text.contains("cracking") || text.contains("stiff")) vata++;
        if (text.contains("pain") || text.contains("joint") || text.contains("muscle")) vata++;

        if (text.contains("burning") || text.contains("heat") || text.contains("inflammation")) pitta++;

        if (text.contains("heavy") || text.contains("swelling") || text.contains("slow")) kapha++;

        if (vata >= pitta && vata >= kapha) return "Vata Prakruti";
        if (pitta >= vata && pitta >= kapha) return "Pitta Prakruti";
        return "Kapha Prakruti";
    }
}
