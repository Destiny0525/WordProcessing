import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/* Implemented by Siyu Dong
 * Start date: 2015-03-17	
 */

/** An instance for words processing in files. */
public class WordProcessing {

	private static HashMap<String, Integer> map; // word/counter pair;
	private static List<Double> num; // number of words of each line;
	
	/** Main method to output the results*/
	public static void main(String[] args) throws IOException {
		String wc_input= args[0];
		String wc_result= args[1];
		String med_result= args[2];
		map= new HashMap<>();
		num= new ArrayList<>();
		readFile(wc_input);

		// Sort the word/counter pair on words.
		List<String> sortedkey= new ArrayList<>(map.keySet());
		Collections.sort(sortedkey);

		// Write wc result into wc_result.txt;
		File wc= new File(wc_result).getAbsoluteFile();
		BufferedWriter bwc= new BufferedWriter(new FileWriter(wc));
		for (String key : sortedkey) {
			String pair= buildString(key,map.get(key));
			bwc.write(pair);
			bwc.newLine();
		}
		bwc.close();
		

		// Compute median value.
		List<Double> copynum= new ArrayList<>(num);
		for (int i= num.size() - 1; i>= 0; i--) {
			List<Double> sortednum= new ArrayList<>(copynum);
			Collections.sort(sortednum);
			int size= sortednum.size();
			double median= size % 2 == 0 ? 
					(sortednum.get(size/2) + sortednum.get(size/2 - 1)) / 2 :
						sortednum.get(size/2);
					num.remove(i);
					num.add(i, median);
					copynum.remove(i);
		}

		// Write median result into med_result.
		File med= new File(med_result).getAbsoluteFile();
		BufferedWriter bmed= new BufferedWriter(new FileWriter(med));
		for (double median : num) {
			bmed.write("" + median);
			bmed.newLine();
		}
		bmed.close();
		
	}

	// Read the wc_input file, save word/counter pair and 
	// number of words in each line. 
	private static void readFile(String wc_input) throws IOException{
		File f= new File(wc_input).getAbsoluteFile();
		File[] files = f.listFiles(); 
		Arrays.sort(files); // Sort files in alphabetical order.
		for (File file : files) {  
			if (!file.getName().endsWith("txt")) continue;
			BufferedReader br= new BufferedReader(new FileReader(file));
			String line;
			while ((line= br.readLine()) != null) {
				readLine(line);
			}
		}  
	}

	// Helper method: read a string, counting the number of words, and how many
	// times a certain word appear.
	private static void readLine(String s) {
		String line= new String(s);
		line= validify(line).trim();
		int n= 0;
		while (line.length() != 0) {
			int index= line.indexOf(' ');
			String word= index == -1 ? line : line.substring(0,index);
			word= word.toLowerCase();
			n= n + 1;

			// Add the word counter 1 iff it exits in map, or add it to map.
			if (!map.isEmpty()  &&  map.containsKey(word)) 
				map.replace(word, map.get(word) + 1);
			else map.put(word, 1);

			// Update line to find the next word.
			line= index == -1 ? "" : line.substring(index + 1).trim();
		}
		num.add((double)n);
	}

	// Helper method: replace all characters other than letters and space 
	// into space.
	private static String validify(String s) {
		for (int i= 0; i< s.length(); i++) {
			if (!Character.isLetter(s.charAt(i))  &&  s.charAt(i) != ' ') {
				if (i< s.length() -1)
					s= s.substring(0,i) + " " + s.substring(i+1);
				else s= s.substring(0,i) + " ";
			}
		}
		return s;
	}
	
	// Helper method: keep same length between the first letter of word and 
	// the counter value when output as a string.
	private static String buildString(String s, int c) {
		int length= 20;
		String result= s;
		for (int i= 0; i<= length - s.length(); i++) {
			result= result + " ";
		}
		result= result + c;
		return result;
	}
}
