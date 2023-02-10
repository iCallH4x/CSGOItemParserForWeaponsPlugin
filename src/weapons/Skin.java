package weapons;

import java.util.ArrayList;
import java.util.List;

public class Skin {
	private String name;
	private List<String> weapons = new ArrayList<>();
	private int id;
	private String tag;
	private String lang;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getWeapons() {
		return weapons;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getLang() {
		String phase = "";
		String marbleized = "";

		if (name.contains("phase")) {
			phase = name.substring(name.indexOf("phase"));
		}
		if (name.contains("marbleized")) {
			marbleized = name.substring(name.indexOf("marbleized"));
		}
		return lang + phase + marbleized;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String weapons() {
		StringBuilder sb = new StringBuilder();
		for (String weapon : weapons) {
			sb.append(weapon).append(";");
		}
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		Skin skin = (Skin) obj;
		return name.equals(skin.name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}