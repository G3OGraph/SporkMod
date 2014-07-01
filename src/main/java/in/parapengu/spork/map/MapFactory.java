package in.parapengu.spork.map;

import in.parapengu.spork.exception.map.MapCreateException;
import in.parapengu.spork.map.features.Contributor;
import in.parapengu.spork.util.Log;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MapFactory {

	private List<MapLoader> loaders;

	public MapFactory() {
		this.loaders = new ArrayList<>();
	}

	public List<MapLoader> getLoaders() {
		return loaders;
	}

	public void load(File folder) {
		if(!folder.isDirectory()) {
			return;
		}

		MapLoader loader = null;
		try {
			loader = map(folder);
		} catch(MapCreateException ex) {
			Log.exception(ex);
		}

		if(loader != null) {
			loaders.add(loader);
			return;
		}

		File[] files = folder.listFiles();
		for(File file : files) {
			if(!file.isDirectory()) {
				continue;
			}

			load(folder);
		}
	}

	public MapLoader load(File folder, Document document) {
		Element root = document.getRootElement();

		String name = root.getChildText("name");
		String version = root.getChildText("version");
		String objective = root.getChildText("objective");
		List<Contributor> authors = contributions(root, "author");
		List<Contributor> contributors = contributions(root, "contributor");

		// name isn't null
		// version isn't null
		// objective isn't null
		// authors isn't empty

		return new MapLoader(folder, document, name, version, objective, authors, contributors);
	}

	public List<Contributor> contributions(Element root, String name) {
		List<Contributor> contributors = new ArrayList<>();

		String plural = name + "s";
		Element list = root.getChild(plural);
		if(list == null) {
			return contributors;
		}

		List<Element> children = list.getChildren(name);
		for(Element child : children) {
			String username = child.getText();
			String contribution = child.getAttributeValue("contribution");
			contributors.add(new Contributor(username, contribution));
		}

		return contributors;
	}

	public MapLoader map(File folder) throws MapCreateException {
		File xml = new File(folder, "map.xml");
		boolean xmlExists = xml.exists() && !xml.isDirectory();
		File region = new File(folder, "region");
		boolean regionExists = region.exists() && region.isDirectory();
		File level = new File(folder, "level.dat");
		boolean levelExists = level.exists() && !level.isDirectory();

		if(xmlExists && regionExists && levelExists) {
			SAXBuilder sax = new SAXBuilder();
			Document document;
			try {
				document = sax.build(xml);
				return load(folder, document);
			} catch(Exception ex) {
				throw new MapCreateException(folder, ex);
			}
		}

		return null;
	}

}
