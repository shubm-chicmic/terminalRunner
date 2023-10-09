package com.example.terminalrunner;

//import edu.cmu.sphinx.api.Configuration;
//import edu.cmu.sphinx.api.SpeechResult;
//import edu.cmu.sphinx.api.StreamSpeechRecognizer;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import java.io.IOException;

public class RecordSpeech {


//    public void captureAudio() {
//        try {
//            // Set up audio format
//            AudioFormat audioFormat = new AudioFormat(
//                    AudioFormat.Encoding.PCM_SIGNED,
//                    44100, // Sample rate (you can adjust this)
//                    16,    // Sample size in bits
//                    2,     // Number of channels (1 for mono, 2 for stereo)
//                    4,     // Frame size (2 bytes per sample per channel)
//                    44100, // Frame rate (samples per second)
//                    false  // Big-endian or little-endian
//            );
//
//            // Get the default microphone as the audio input source
//            TargetDataLine line;
//            DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
//            if (!AudioSystem.isLineSupported(info)) {
//                System.err.println("Audio line not supported.");
//                return;
//            }
//            line = (TargetDataLine) AudioSystem.getLine(info);
//            line.open(audioFormat);
//            line.start();
//
//            // Capture audio data
//            byte[] buffer = new byte[4096]; // Adjust the buffer size as needed
//            int bytesRead;
//            while () {
//                bytesRead = line.read(buffer, 0, buffer.length);
//
//                // Process the audio data (you can send it to the speech recognition engine)
//                // For simplicity, this example doesn't include speech recognition logic.
//            }
//
//            // Stop and close the audio line when done
//            line.stop();
//            line.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void recordSpeech() {
        try {
            // Initialize FreeTTS
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            Synthesizer synthesizer = Central.createSynthesizer(null);
            synthesizer.allocate();
            synthesizer.resume();

            // Start voice listening
            synthesizer.speakPlainText("Listening...", null);

            // Implement your timeout logic here (e.g., wait for 4 seconds)

            // Simulate voice input (replace this with actual audio input)
            String voiceInput = "This is a test.";

            // Convert the voice input to text
            String recognizedText = voiceInput;

            // Display or process the recognized text
            System.out.println("Recognized Text: " + recognizedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void record(){
        Configuration configuration = new Configuration();

        configuration.setAcousticModelPath("/home/chicmic/IdeaProjects/terminalRunner/terminalRunner/src/main/resources/edu/cmu/sphinx/models/en-us/en-us");
       configuration.setDictionaryPath("/home/chicmic/IdeaProjects/terminalRunner/terminalRunner/src/main/resources/dictionary.dic");
        configuration.setLanguageModelPath("/home/chicmic/IdeaProjects/terminalRunner/terminalRunner/src/main/resources/lanModel.lm");

        try {
            LiveSpeechRecognizer speech = new LiveSpeechRecognizer(configuration);
            speech.startRecognition(true);

            SpeechResult speechResult = null;

            while ((speechResult = speech.getResult()) != null) {
                String voiceCommand = speechResult.getHypothesis();
                System.out.println("Voice Command is " + voiceCommand);

                if (voiceCommand.equalsIgnoreCase("Open Chrome")) {
                    Runtime.getRuntime().exec("cmd.exe /c start chrome www.infybuzz.com");
                } else if (voiceCommand.equalsIgnoreCase("Close Chrome")) {
                    Runtime.getRuntime().exec("cmd.exe /c TASKKILL /IM chrome.exe");
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
