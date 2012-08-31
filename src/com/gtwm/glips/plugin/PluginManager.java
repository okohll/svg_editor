package com.gtwm.glips.plugin;

import java.util.HashSet;
import java.util.Set;

import fr.itris.glips.svgeditor.Editor;

public class PluginManager {

	public PluginManager(Editor editor) {
		this.editor = editor;
	}
	
	public void addPlugin(Plugin plugin) {
		this.plugins.add(plugin);
	}
	
	public void initializePlugins() {
		for (Plugin plugin : plugins) {
			plugin.init();
		}
	}
	
	private Set<Plugin> plugins = new HashSet<Plugin>();
	
	private final Editor editor;
}
