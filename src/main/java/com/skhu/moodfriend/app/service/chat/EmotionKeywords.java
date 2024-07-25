package com.skhu.moodfriend.app.service.chat;

import java.util.List;

public record EmotionKeywords(List<String> keywords) {

    public static final EmotionKeywords EMOTION_KEYWORDS = new EmotionKeywords(List.of(
            // Positive Emotions
            "happy", "joy", "excited", "pleased", "delighted", "content",
            "satisfied", "cheerful", "elated", "grateful", "optimistic", "hopeful",
            "confident", "proud", "enthusiastic", "inspired", "lively", "energetic",
            "proud", "overjoyed", "thrilled", "euphoric", "jubilant",

            // Negative Emotions
            "sad", "depressed", "unhappy", "disappointed", "gloomy", "down",
            "melancholic", "dismayed", "despondent", "blue", "mournful", "woeful",
            "angry", "furious", "irritated", "annoyed", "enraged", "outraged",
            "frustrated", "exasperated", "agitated", "resentful", "infuriated",

            // Fear & Anxiety
            "afraid", "scared", "nervous", "anxious", "worried", "terrified",
            "panicked", "apprehensive", "uneasy", "fearful", "dreadful",
            "jittery", "shaky", "timid", "startled", "suspicious",

            // Boredom & Indifference
            "bored", "tired", "weary", "disinterested", "apathetic", "unmotivated",
            "indifferent", "unengaged", "listless", "lackluster",

            // Surprise & Amazement
            "surprised", "shocked", "astonished", "amazed", "dumbfounded",
            "startled", "stunned", "bewildered", "flabbergasted", "impressed",
            "incredulous",

            // Confusion & Uncertainty
            "confused", "perplexed", "bewildered", "uncertain", "dazed",
            "disoriented", "lost", "hesitant", "doubtful",

            // Other Emotions
            "guilty", "embarrassed", "ashamed", "regretful", "jealous",
            "envious", "resentful", "cautious", "skeptical", "lonely",
            "isolated", "hopeful", "disconnected", "distant", "miserable",

            // Additional Emotions
            "resentful", "disgusted", "displeased", "overwhelmed", "suspicious",
            "serene", "tranquil", "contented", "nostalgic", "sentimental",
            "indignant", "distressed", "dismayed", "fascinated", "curious",
            "optimistic", "pessimistic", "melancholic", "jaded", "sensitive",
            "empathetic", "compassionate", "devastated", "elated", "exasperated",
            "resigned", "ecstatic", "euphoric", "grateful", "rejuvenated"
    ));
}
