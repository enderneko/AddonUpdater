package enderneko.addonupdater.dao.impl;

import java.io.File;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import enderneko.addonupdater.dao.IAddonDAO;
import enderneko.addonupdater.domain.Addon;
import enderneko.addonupdater.util.DOMUtil;

public class AddonDAOImpl implements IAddonDAO {
	private static File f;

	static {
		f = new File(System.getProperty("user.dir") + "\\db\\addons.xml");
	}

	@Override
	public void add(Addon a) {
		Document doc = DOMUtil.getDocument(f);
		Element addonEl = doc.createElement("addon");
		Element nameEl = doc.createElement("name");
		Element versionEl = doc.createElement("version");
		Element authorEl = doc.createElement("author");
		Element statusEl = doc.createElement("status");
		Element urlEl = doc.createElement("url");
		Element latestVersionEl = doc.createElement("latestVersion");
		Element latestDateEl = doc.createElement("latestDate");
		Element latestFileEl = doc.createElement("latestFile");

		nameEl.setTextContent(a.getName());
		versionEl.setTextContent(a.getVersion());
		authorEl.setTextContent(a.getAuthor());
		statusEl.setTextContent(a.getStatus());
		urlEl.setTextContent(a.getUrl());
		latestVersionEl.setTextContent(a.getLatestVersion());
		latestDateEl.setTextContent(a.getLatestDate());
		latestFileEl.setTextContent(a.getLatestFile());

		doc.getDocumentElement().appendChild(addonEl);
		addonEl.appendChild(nameEl);
		addonEl.appendChild(versionEl);
		addonEl.appendChild(authorEl);
		addonEl.appendChild(statusEl);
		addonEl.appendChild(urlEl);
		addonEl.appendChild(latestVersionEl);
		addonEl.appendChild(latestDateEl);
		addonEl.appendChild(latestFileEl);

		DOMUtil.writeToXML(doc, f);
	}

	@Override
	public Addon get(String name) {
		Document doc = DOMUtil.getDocument(f);
		NodeList list = doc.getElementsByTagName("addon");
		for (int i = 0; i < list.getLength(); i++) {
			Element addonEl = (Element) list.item(i);
			if (name.equals(addonEl.getElementsByTagName("name").item(0).getTextContent())) {
				return elementToAddon(addonEl);
			}
		}
		return null;
	}

	@Override
	public boolean isManaged(String name) {
		Document doc = DOMUtil.getDocument(f);
		NodeList list = doc.getElementsByTagName("addon");
		for (int i = 0; i < list.getLength(); i++) {
			Element addonEl = (Element) list.item(i);
			if (name.equals(addonEl.getElementsByTagName("name").item(0).getTextContent())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Vector<Addon> getAll() {
		Vector<Addon> addons = new Vector<>();

		Document doc = DOMUtil.getDocument(f);
		NodeList list = doc.getElementsByTagName("addon");
		for (int i = 0; i < list.getLength(); i++) {
			Element addonEl = (Element) list.item(i);
			addons.add(elementToAddon(addonEl));
		}
		return addons;
	}

	@Override
	public void update(Addon a) {
		delete(a.getName());
		add(a);
	}

	@Override
	public void delete(String name) {
		Document doc = DOMUtil.getDocument(f);
		NodeList list = doc.getElementsByTagName("addon");
		for (int i = 0; i < list.getLength(); i++) {
			Element addonEl = (Element) list.item(i);
			if (name.equals(addonEl.getElementsByTagName("name").item(0).getTextContent())) {
				addonEl.getParentNode().removeChild(addonEl);
				DOMUtil.writeToXML(doc, f);
				break;
			}
		}
	}

	private Addon elementToAddon(Element addonEl) {
		String name = addonEl.getElementsByTagName("name").item(0).getTextContent();
		String version = addonEl.getElementsByTagName("version").item(0).getTextContent();
		String author = addonEl.getElementsByTagName("author").item(0).getTextContent();
		String status = addonEl.getElementsByTagName("status").item(0).getTextContent();
		String url = addonEl.getElementsByTagName("url").item(0).getTextContent();
		String latestVersion = addonEl.getElementsByTagName("latestVersion").item(0).getTextContent();
		String latestDate = addonEl.getElementsByTagName("latestDate").item(0).getTextContent();
		String latestFile = addonEl.getElementsByTagName("latestFile").item(0).getTextContent();

		return new Addon(name, 
				version == "" ? null : version,
				author == "" ? null : author,
				status == "" ? null : status,
				url == "" ? null : url,
				latestVersion == "" ? null : latestVersion,
				latestDate == "" ? null : latestDate,
				latestFile == "" ? null : latestFile);
	}
}
