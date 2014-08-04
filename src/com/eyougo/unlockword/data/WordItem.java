package com.eyougo.unlockword.data;

public class WordItem {
	private String word;
	private String trans;
	private String phonetic;
	private String tags;
	private int process;
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getTrans() {
		return trans;
	}
	public void setTrans(String trans) {
		this.trans = trans;
	}
	public String getPhonetic() {
		return phonetic;
	}
	public void setPhonetic(String phonetic) {
		this.phonetic = phonetic;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public int getProcess() {
		return process;
	}
	public void setProcess(int process) {
		this.process = process;
	}
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
        if(o == null) return false;
        if(getClass() != o.getClass()) return false;
        WordItem other = (WordItem)o;
        if (this.word == other.getWord()) {
			return true;
		}
        if (this.word == null){
        	return false;
        }
		return this.word.equals(other.getWord());
	}
	
}
