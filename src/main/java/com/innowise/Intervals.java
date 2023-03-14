package com.innowise;

import java.util.Arrays;
import java.util.List;

public class Intervals {

    public static final int[] staveInSemitone = {0, 2, 4, 5, 7, 9, 11};
    public static final List<String> staveInNote = Arrays.asList("C", "D", "E", "F", "G", "A", "B");


    public static String intervalConstruction(String[] args) {

        inputArrayValidation(args);

        String intervalToBuild = args[0];

        char[] initialNote = args[1].toCharArray();

        boolean dscDirection;
        dscDirection = args.length > 2 && args[2].equals("dsc");
        String resultNote;

        if (intervalToBuild.equals("P8")) {
            resultNote = String.valueOf(initialNote);
        } else {
            String resultNoteWithoutSign = findResultNoteWithoutSign(initialNote, intervalToBuild, dscDirection);

            int positionInSemitones = countResultNotePositionInSemitones(initialNote, intervalToBuild, dscDirection);

            resultNote = findResultNote(resultNoteWithoutSign, positionInSemitones);

        }

        return resultNote;
    }


    private static String findResultNote(String resultNoteWithoutSign, int positionInSemitones) {

        StringBuilder resultNote = new StringBuilder(resultNoteWithoutSign);
        int resultNoteIndex = staveInNote.indexOf(resultNoteWithoutSign);
        int degreesInSemitone = staveInSemitone[resultNoteIndex];
        int difference = (Math.abs(degreesInSemitone - positionInSemitones) % 12);

        if (degreesInSemitone > positionInSemitones) {
            resultNote.append("b".repeat(Math.max(0, difference)));
        } else {
            resultNote.append("#".repeat(Math.max(0, difference)));
        }
        return resultNote.toString();
    }


    private static int countResultNotePositionInSemitones(char[] initialNote, String intervalToBuild, boolean dscDirection) {
        //find amount of semitones
        int semitones = countSemitonesInInterval(intervalToBuild);
        int initialNoteIndex = staveInNote.indexOf(String.valueOf(initialNote[0]));

        int positionInSemitones;

        if (dscDirection) {
            positionInSemitones = (12 + staveInSemitone[initialNoteIndex] - semitones) % 12;

        } else {
            positionInSemitones = (staveInSemitone[initialNoteIndex] + semitones) % 12;
        }
        //looking for b or #
        if (initialNote.length > 1) {
            if (initialNote[1] == 'b') {
                positionInSemitones -= 1;
            } else if (initialNote[1] == '#') {
                positionInSemitones += 1;
            }
        }
        return positionInSemitones;
    }

    //initial note + degrees
    private static String findResultNoteWithoutSign(char[] initialNote, String interval, boolean dscDirection) {

        String resultNote;

        int degrees = Character.digit(interval.charAt(1), 10);
        int initialNoteIndex = staveInNote.indexOf(String.valueOf(initialNote[0]));
        int resultNoteIndex;

        //find result note counting degrees
        if (dscDirection) {
            resultNoteIndex = (7 + initialNoteIndex - (degrees - 1)) % 7;
        } else {
            resultNoteIndex = (initialNoteIndex + degrees - 1) % 7;
        }
        resultNote = staveInNote.get(resultNoteIndex);

        return resultNote;
    }

    private static int countSemitonesInInterval(String interval) {

        int semitones;
        int degrees = Character.digit(interval.charAt(1), 10);
        //semitones between initial note and result note
        if (interval.charAt(0) == 'm') {
            //for minor go down from C
            semitones = 12 - staveInSemitone[7 - (degrees - 1)];
        } else {
            //for Major go up from C
            semitones = staveInSemitone[degrees - 1];
        }
        return semitones;
    }

    public static String intervalIdentification(String[] args) {

        inputArrayValidation(args);

        char[] firstNote = args[0].toCharArray();
        char[] secondNote = args[1].toCharArray();
        boolean dscDirection;

        dscDirection = args.length > 2 && args[2].equals("dsc");
        if (dscDirection) {
            char[] temp = firstNote;
            firstNote = secondNote;
            secondNote = temp;
        }

        String interval;
        if (Arrays.equals(firstNote, secondNote)) {
            interval = "P8";
        } else {
            int semitonesBetweenNotes = countSemitonesBetweenNotes(firstNote, secondNote);
            interval = findInterval(semitonesBetweenNotes, firstNote, secondNote);
        }
        return interval;
    }

    private static String findInterval(int semitones, char[] firstNote, char[] secondNote) {
        String degrees = String.valueOf(findDegrees(firstNote, secondNote));
        String letter = findIntervalLetter(semitones);
        return letter.concat(degrees);
    }

    private static String findIntervalLetter(int semitones) {
        return switch (semitones) {
            case 1, 3, 8, 10 -> "m";
            case 2, 4, 9, 11 -> "M";
            case 5, 7, 12 -> "P";
            default -> "Interval does not exist";
        };

    }

    private static int findDegrees(char[] firstNote, char[] secondNote) {
        int firstNoteIndex = staveInNote.indexOf(String.valueOf(firstNote[0]));
        int secondNoteIndex = staveInNote.indexOf(String.valueOf(secondNote[0]));
        int degrees;
        if (secondNoteIndex >= firstNoteIndex) {
            degrees = secondNoteIndex - firstNoteIndex + 1;
        } else {
            degrees = 7 - (firstNoteIndex - (secondNoteIndex + 1));
        }
        if (degrees == 1) {
            throw new RuntimeException("ICannot identify the interval");
        }
        return degrees;
    }

    private static int countSemitonesBetweenNotes(char[] firstNote, char[] secondNote) {

        int semitones;
        int firstNoteIndex = staveInNote.indexOf(String.valueOf(firstNote[0]));
        int secondNoteIndex = staveInNote.indexOf(String.valueOf(secondNote[0]));
        if (secondNoteIndex < firstNoteIndex) {
            semitones = Math.abs(12 - (staveInSemitone[firstNoteIndex] - staveInSemitone[secondNoteIndex])) % 12;
        } else {
            semitones = (staveInSemitone[secondNoteIndex] - staveInSemitone[firstNoteIndex]);
        }

        int amountOfSignFirst = firstNote.length - 1;
        if (firstNote.length > 1) {
            if (firstNote[1] == '#') {
                amountOfSignFirst = -amountOfSignFirst;
            }
            semitones += amountOfSignFirst;
        }

        int amountOfSignSecond = secondNote.length - 1;
        if (secondNote.length > 1) {
            if (secondNote[1] == 'b') {
                amountOfSignSecond = -amountOfSignSecond;
            }
            semitones += amountOfSignSecond;
        }
        return semitones;
    }

    private static void inputArrayValidation(String[] args) {
        if (args.length > 3 || args.length < 1) {
            throw new RuntimeException("Illegal number of elements in input array");
        }
    }
}
