/*
    This program computes letter and word frequencies
    on a subset of documents in the Gutenberg corpus
    Author: V. Gudivada
    Date Created: 3 March 2016
    Date Last Modified: 3 March 2016
*/

// represents files and directory pathnames 
// in an abstract manner
import java.io.File;

// reads data from files as streams of characters
import java.io.FileReader;

// reads text efficiently from character-input
// stream buffers 
import java.io.BufferedReader;

// signals that an input/output (I/O) exception 
// of some kind has occurred
import java.io.IOException;

// compiled representation of a regular expressions
import java.util.regex.Pattern;

// matches a compiled regular expression with an input string
import java.util.regex.Matcher;

import java.util.*;
import java.io.*;

public class TextProcessing {

	public static void main(String[] args) {

		// did the user provide correct number of command line arguments?
		// if not, print message and exit
		if (args.length != 1) {
			System.err.println("Number of command line arguments must be 1");
			System.err.println("You have given only " + args.length + "command line arguments");
			System.err.println("Incorrect usage. Program terminated");
			System.err.println("Correct usage: java TextProcessingV3 <input-file-name>");
			System.exit(1);
		}

		// extract input file name from command line arguments
		// this is the name of the file from the Gutenberg corpus
		String inFileName = args[0];
		System.out.println("Input file name is: " + inFileName);

		// br for efficiently reading characters from an input stream
		BufferedReader br = null;

		// wordPattern specifies pattern for words using a regular expression
		Pattern wordPattern = Pattern.compile(">[^<]+");

		// wordMatcher finds words by spotting word word patterns with input
		Matcher wordMatcher;

		// a line read from file
		String line;

		// an extracted word from a line
		String word;

		Map<String, Integer> map = new HashMap<String, Integer>();

		FileInputStream fis;
		Map<String, String> wordMap = new HashMap<String, String>();
		try {
			fis = new FileInputStream("javatxt.txt");
			Scanner sc = new Scanner(fis);
			while (sc.hasNextLine()) {
				String l = sc.nextLine();
				String key = l.substring(0, l.indexOf(" "));
				String value = l.substring(l.indexOf(" "));
				wordMap.put(key.toLowerCase(), value.toLowerCase().trim());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// open the input file, read one line at a time, extract words
		// in the line, extract characters in a word, write words and
		// character counts to disk files
		try {
			// get a BufferedReader object, which encapsulates
			// access to a (disk) file
			br = new BufferedReader(new FileReader(inFileName));

			// as long as we have more lines to process, read a line
			// the following line is doing two things: makes an assignment
			// and serves as a boolean expression for while test
			while ((line = br.readLine()) != null) {

				// process the line by extracting words using the wordPattern
				wordMatcher = wordPattern.matcher(line);

				// process one word at a time
				while (wordMatcher.find()) {

					// extract the word
					word = line.substring(wordMatcher.start(), wordMatcher.end());
					word = word.replace(">", "");

					// convert the word to lower-case
					word = word.toLowerCase();

					for (Map.Entry<String, String> entry : wordMap.entrySet()) {
						String key = entry.getKey();
						String value = entry.getValue();

						word = word.replace(key, value);
					}

					// word = word.replaceAll("[,.;?!-]", " ");
					// word = word.replace(":"," ");

					StringTokenizer st = new StringTokenizer(word, ",.;?!-: ");
					while (st.hasMoreTokens()) {
						String w = st.nextToken();

						Integer f = map.get(w);
						if (f == null) {
							map.put(w, 1);
						} else {
							map.put(w, f + 1);
						}
					}

				} // while - wordMatcher
			} // while - line
		} // try
		catch (IOException ex) {
			System.err.println("File " + inFileName + " not found. Program terminated.\n");
			System.exit(1);
		}

		List<Map.Entry<String, Integer>> entries = new ArrayList(map.entrySet());
		Collections.sort(entries, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

		float total = 0;

		for (Map.Entry<String, Integer> entry : entries) {
			total += entry.getValue();
		}

		for (Map.Entry<String, Integer> entry : entries) {
			System.out.printf("%-20s %10d %10.2f\n", entry.getKey(), entry.getValue(), entry.getValue() / total);
		}

		/*
		 * Iterator<String> mapIter = wordMap.keySet().iterator();
		 * while(mapIter.hasNext()){ String key = mapIter.next();
		 * System.out.println( key); System.out.println(wordMap.get(key)); }
		 */

	}
}