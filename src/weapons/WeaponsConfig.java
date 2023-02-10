
package weapons;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeaponsConfig {

	private static String CSGO_FOLDER;
	private static String ITEMS_GAME;
	private static String LANGFILE;
	private static String OUTPUT_FILE;
	private static String ENLANGFILE;
	private static String LANG;

	public static void main(String[] args) {

		if (args.length != 2) {
			System.out.println("Wrong number of arguements");
			return;
		}

		List<Skin> list = new ArrayList<Skin>();
		CSGO_FOLDER = args[0];
		LANG = args[1];
		ITEMS_GAME = CSGO_FOLDER + "/scripts/items/items_game.txt";
		LANGFILE = CSGO_FOLDER + "/resource/csgo_" + LANG + ".txt";
		ENLANGFILE = CSGO_FOLDER + "/resource/csgo_english.txt";
		OUTPUT_FILE = CSGO_FOLDER + "/addons/sourcemod/configs/weapons/weapons_" + args[1] + ".cfg";

		String sCurrentLine;
		StringBuilder sb;

		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(LANGFILE), StandardCharsets.UTF_16LE));
			 BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OUTPUT_FILE), StandardCharsets.UTF_8));
			 BufferedReader br2 = new BufferedReader(new FileReader(ITEMS_GAME))) {

			sb = new StringBuilder();
			while ((sCurrentLine = br.readLine()) != null) {
				sb.append(sCurrentLine);
			}
			String translations = sb.toString();

			String translationsEn = null;


			sb = new StringBuilder();
			while ((sCurrentLine = br2.readLine()) != null) {
				sb.append(sCurrentLine);
				sb.append(System.lineSeparator());
			}
			String items_game = sb.toString();

			Pattern p = Pattern.compile("\"(\\d+?)\"[\\r\\n]{1,2}\\s*?\\{[\\r\\n]{1,2}\\s*?\"name\"\\s*?\"([^\"]*?)\"[\\r\\n]{1,2}[^\\r\\n]*?[\\r\\n]{1,2}\\s*?\"description_tag\"\\s*?\"#(Paint[Kk]it_[^\"]*?)\"");
			Matcher m = p.matcher(items_game);
			while (m.find()) {
				Skin skin = new Skin();
				skin.setId(Integer.parseInt(m.group(1)));
				skin.setName(m.group(2));
				skin.setTag(m.group(3));
				Pattern pp = Pattern.compile("\\s*?\"icon_path\"\\s*?\"econ/default_generated/(weapon_[^\"]*?)_" + skin.getName() + "_medium\"");
				Matcher mm = pp.matcher(items_game);
				while (mm.find()) {
					String weapon = mm.group(1);
					skin.getWeapons().add(weapon);
				}
				Pattern ppp = Pattern.compile("\"" + skin.getTag() + "\"\\s*?\"([^\"]*?)\"", Pattern.CASE_INSENSITIVE);
				Matcher mmm = ppp.matcher(translations);
				if (mmm.find()) {
					skin.setLang(mmm.group(1));
				}
				if(translationsEn != null && skin.getLang() == null) {
					mmm = ppp.matcher(translationsEn);
					if (mmm.find()) {
						skin.setLang(mmm.group(1));
					}
				}
				list.add(skin);
			}

			bw.write("\"Skins\"" + System.lineSeparator());
			bw.write("{" + System.lineSeparator());
			bw.flush();
			for (int i = list.size() -1; i >= 0; i--) {
				Skin skin = list.get(i);
				if (skin.getWeapons().size() > 0) {
					bw.write("\t\""+ skin.getLang() + "\"" + System.lineSeparator());
					bw.write("\t{" + System.lineSeparator());
					bw.write("\t\t\"index\"\t\t\"" + skin.getId() + "\"" + System.lineSeparator());
					bw.write("\t\t\"classes\"\t\"" + skin.weapons() + "\"" + System.lineSeparator());
					bw.write("\t}" + System.lineSeparator());
				}
				bw.flush();
			}
			bw.write("}" + System.lineSeparator());
			bw.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}