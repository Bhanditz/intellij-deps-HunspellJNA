package dk.dren.hunspell;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestDictionary {

	private static final String MISSPELLED = "Misspelled word not found : ";
	private static final String CORRECTLY_SPELLED = "Correctly spelled word invalid reported as misspelled ";
	private static final String DK_WORDS[] = { "ymerfest", "gr\u00f8ftegraver", "h\u00e6ngeplante",
			"\u00e6selm\u00e6lk" };

	private static final String dk_words_misspelled[] = { "y0merfest", "g0r\u00f8ftesgraver",
			"h\u00e6ngesplante", "\u00e6selsm\u00e6lk" };

	private static final String de_words[] = { "Donnerdampfschifffahrt",
			"Donnerdampfschifffahrtsgesellschaftskapit\u00e4n", "Messer",
			"muss" };
	private static final String de_words_misspelled[] = { "Donnnerdampfschifffahrt",
			"Donnnerdampfschiffffahrtsgesellschaftskapit\u00e4n", "Messser",
			"musss" };

	private String dir;
	public static final String languageDA = "da_DK";
	public static final String languageDE = "de_DE";
	private Hunspell instance;
	private Hunspell.Dictionary da;
	private Hunspell.Dictionary de;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		System.err.println("Loading Hunspell");
		dir = System.getProperty("user.dir") + File.separator + "testdata";

		instance = Hunspell.getInstance();
		da = instance.getDictionary(dir + "/" + languageDA);
		de = instance.getDictionary(dir + "/" + languageDE);
	}

	@Test
	@Ignore("Neither ymerfest nor gr\u00f8ftegraver are found in the latest (v2.1) Danish dictionary <http://www.stavekontrolden.dk/main/top/index.php>")
	public void testMisspelled() {
		for (String word : dk_words_misspelled) {
			assertTrue(MISSPELLED + word, da.misspelled(word));
		}
		for (String word : de_words_misspelled) {
			assertTrue(MISSPELLED + word, de.misspelled(word));
		}
		for (String word : DK_WORDS) {
			assertFalse(CORRECTLY_SPELLED + word, da.misspelled(word));
		}
		for (String word : de_words) {
			assertFalse(CORRECTLY_SPELLED + word, de.misspelled(word));
		}

	}

	@Test
	@Ignore("Neither ymerfest nor gr\u00f8ftegraver are found in the latest (v2.1) Danish dictionary <http://www.stavekontrolden.dk/main/top/index.php>")
	public void testSuggest() {
		int i = 0;
		for (String word : dk_words_misspelled) {
			List<String> suggest = da.suggest(word);
			// System.out.println("Found : " + suggest);
			assertTrue("Suggestions did not contain " + DK_WORDS[i],
					suggest.contains(DK_WORDS[i++]));
		}
	}

	@Test
	public void testAnalyze() {
		// TODO actually do some test here for now (and forseeable future just print)
		for (String word : DK_WORDS) {
			List<String> analyze = da.analyze(word);
			System.out.println("DK analyze : " + analyze);
		}
		for (String word : de_words) {
			List<String> analyze = de.analyze(word);
			System.out.println("DE analyze : " + analyze);
		}
	}

	@Test
	public void testStem() {
		// TODO actually do some test here for now (and forseeable future just print)
		for (String word : DK_WORDS) {
			List<String> stem = da.stem(word);
			System.out.println("DK stem : " + stem);
		}
		for (String word : de_words) {
			List<String> stem = de.stem(word);
			System.out.println("DE stem : " + stem);
		}
	}
	
	@Test
	public void testAdd() throws FileNotFoundException, UnsupportedEncodingException {
		final String dicPath = dir + File.separator + "test";
		instance = Hunspell.getInstance();
		final Hunspell.Dictionary dictionary = instance.getDictionary(dicPath);
		final String somenewword = "somenewword";
		try {
			assertTrue(dictionary.misspelled(somenewword));
			dictionary.add(somenewword);
			assertFalse(dictionary.misspelled(somenewword));
		}finally {
			// return the state back
			dictionary.remove(somenewword);
		}
	}
	
	@Test
	public void testRemove() throws FileNotFoundException, UnsupportedEncodingException {
		final String dicPath = dir + File.separator + "test";
		instance = Hunspell.getInstance();
		final Hunspell.Dictionary dictionary = instance.getDictionary(dicPath);
		final String existingWord = "existingword";
		try {
			assertFalse(dictionary.misspelled(existingWord));
			dictionary.remove(existingWord);
			assertTrue(dictionary.misspelled(existingWord));
		}finally {
			// return the state back
			dictionary.add(existingWord);
		}
	}
	

}
