package main.java;

import java.util.List;

public class HelpTopic {
    private final int id;
    private final String title;
    private final HelpCategory category;
    private final String answerText;
    private final List<String> keywords; // ord der kan matche spørgsmålet

    public HelpTopic(int id, String title, HelpCategory category,
                     String answerText, List<String> keywords) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.answerText = answerText;
        this.keywords = keywords;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public HelpCategory getCategory() {
        return category;
    }

    public String getAnswerText() {
        return answerText;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public boolean matches(String query) {
        String q = query.toLowerCase();

        if (title.toLowerCase().contains(q)) {
            return true;
        }

        for (String keyword : keywords) {
            if (q.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "main.demo.HelpTopic{" +
                "title='" + title + '\'' +
                ", category=" + category +
                '}';
    }
}
