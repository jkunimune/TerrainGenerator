package mechanics;

import mapprojections.Equirectangular;

public final class IslandGeneration {

	public static final void main(String[] args) throws InterruptedException {
		java.applet.AudioClip music;
		try {
			music = java.applet.Applet.newAudioClip(new java.net.URL(
					"file:Sound/holeWaimea.wav"));
		}
		catch (java.net.MalformedURLException error) {
			System.err.println("Song file not found; continuing silently");
			music = null;
		}
		music.loop();
		
		while (true) {
			Island atlantis = new Island(100,100);
			
			Map view = new Equirectangular(atlantis.getSurface(), 500,500);
			
			atlantis.generate(view);
			
			Thread.sleep(20000);
		}
	}

}
