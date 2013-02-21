package be.kdg.groupcandroid;

public class ChatMessage {
	public String content;
	public Integer image;
	public String name;
	public String time;
	
	public ChatMessage(String content, Integer image, String name, String time) {	
		this.content = content;
		this.image = image;
		this.name = name;
		this.time = time;
	}
}
