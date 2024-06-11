package dev.ime.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mongodb.uri")
public class ConfigProperties {

	private String read;
	private String write;
	
	public String getRead() {
		return read;
	}
	public String getWrite() {
		return write;
	}
	public void setRead(String read) {
		this.read = read;
	}
	public void setWrite(String write) {
		this.write = write;
	}
	
}
