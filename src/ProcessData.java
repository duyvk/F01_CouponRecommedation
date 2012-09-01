

import java.util.HashSet;
import java.util.StringTokenizer;

public class ProcessData {
	public static String[] s_stopWords = { "m", "a", "about", "above", "above",
			"across", "after", "afterwards", "again", "against", "all",
			"almost", "alone", "along", "already", "also", "although",
			"always", "am", "among", "amongst", "amoungst", "amount", "an",
			"and", "another", "any", "anyhow", "anyone", "anything", "anyway",
			"anywhere", "are", "around", "as", "at", "back", "be", "became",
			"because", "become", "becomes", "becoming", "been", "before",
			"beforehand", "behind", "being", "below", "beside", "besides",
			"between", "beyond", "bill", "both", "bottom", "but", "by", "call",
			"can", "cannot", "cant", "co", "con", "could", "couldnt", "cry",
			"de", "describe", "detail", "do", "done", "down", "due", "during",
			"each", "eg", "eight", "either", "eleven", "else", "elsewhere",
			"empty", "enough", "etc", "even", "ever", "every", "everyone",
			"everything", "everywhere", "except", "few", "fifteen", "fify",
			"fill", "find", "fire", "first", "five", "for", "former",
			"formerly", "forty", "found", "four", "from", "front", "full",
			"further", "get", "give", "go", "had", "has", "hasnt", "have",
			"he", "hence", "her", "here", "hereafter", "hereby", "herein",
			"hereupon", "hers", "herself", "him", "himself", "his", "how",
			"however", "hundred", "ie", "if", "in", "inc", "indeed",
			"interest", "into", "is", "it", "its", "itself", "keep", "last",
			"latter", "latterly", "least", "less", "ltd", "made", "many",
			"may", "me", "meanwhile", "might", "mill", "mine", "more",
			"moreover", "most", "mostly", "move", "much", "must", "my",
			"myself", "name", "namely", "neither", "never", "nevertheless",
			"next", "nine", "no", "nobody", "none", "noone", "nor", "not",
			"nothing", "now", "nowhere", "of", "off", "often", "on", "once",
			"one", "only", "onto", "or", "other", "others", "otherwise", "our",
			"ours", "ourselves", "out", "over", "own", "part", "per",
			"perhaps", "please", "put", "rather", "re", "same", "see", "seem",
			"seemed", "seeming", "seems", "serious", "several", "she",
			"should", "show", "side", "since", "sincere", "six", "sixty", "so",
			"some", "somehow", "someone", "something", "sometime", "sometimes",
			"somewhere", "still", "such", "system", "take", "ten", "than",
			"that", "the", "their", "them", "themselves", "then", "thence",
			"there", "thereafter", "thereby", "therefore", "therein",
			"thereupon", "these", "they", "thickv", "thin", "third", "this",
			"those", "though", "three", "through", "throughout", "thru",
			"thus", "to", "together", "too", "top", "toward", "towards",
			"twelve", "twenty", "two", "un", "under", "until", "up", "upon",
			"us", "very", "via", "was", "we", "well", "were", "what",
			"whatever", "when", "whence", "whenever", "where", "whereafter",
			"whereas", "whereby", "wherein", "whereupon", "wherever",
			"whether", "which", "while", "whither", "who", "whoever", "whole",
			"whom", "whose", "why", "will", "with", "within", "without",
			"would", "yet", "you", "your", "yours", "yourself", "yourselves",
			"the" };

	public static HashSet<String> stopwords = new HashSet<String>(
			s_stopWords.length);

	public ProcessData() {
		for (String s : s_stopWords) {
			stopwords.add(s);
		}
	}

	// Common Words
	public static int countCommonWord(String firstString, String secondString) {
		int numberCommonWord = 0;
		String[] firstTokens = firstString.split(" ");
		String[] secondTokens = secondString.split(" ");

		for (String firstToken : firstTokens) {
			for (String secondToken : secondTokens) {
				if (firstToken.compareTo(secondToken) == 0) {
					numberCommonWord++;
				}
			}
		}
		return numberCommonWord;
	}

	// Check stopword
	public static boolean isStopword(String s) {
		return stopwords.contains(s);
	}

	// Remove Stopwords
	public static String removeStopwords(String beRemovedString) {
		String resultString = new String();
		StringTokenizer tokenizer = new StringTokenizer(beRemovedString);
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if (!isStopword(token)) {
				resultString += token + " ";

			}
		}
		return resultString;
	}

	// Compute similarity using commond word
	public static double simByCommonWord(String first, String second,
			int threshold) {
		if(first.equals("null") && second.equals("null")){
			return 0;
		}
		int commonWord = ProcessData.countCommonWord(first, second);
		if (commonWord >= threshold) {
			return 1;
		} else {
			int oneLength = new StringTokenizer(first).countTokens();
			int twoLength = new StringTokenizer(second).countTokens();
			return (double) commonWord
					/ ((oneLength > twoLength) ? oneLength : twoLength);
		}
	}
	
	public static double simByNumber(String first, String second,
			int threshold) {
		if(first.trim().compareTo("null")==0 || second.trim().compareTo("null")==0) {
			return 0;
		}else {
			int thisPrice =(int) Math.floor(Double.parseDouble(first.trim()));
			int otherPrice =(int) Math.floor(Double.parseDouble(second.trim()));
			if(Math.abs(thisPrice-otherPrice) < threshold) {
				return 1;
			}
			return 0;
		}
	}

	/**
	 * @param args
	 */
	/*
	 * public static void main(String[] args) { // TODO Auto-generated method
	 * stub ProcessData one = new ProcessData(); System.out .println(one
	 * .removeStopwords(
	 * "Lenovo is the best choice- ThinkPad Edge 03193SU 15.6\" LED Notebook - Intel Core i3 i3-380M 2.53 GHz - Black "
	 * )); System.out.println(one.countCommonWord("hello word one",
	 * "hello star one")); }
	 */
}
