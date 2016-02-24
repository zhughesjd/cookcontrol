package net.joshuahughes.smokercontroller.controller;

public interface Controller {
	public void process(Parameters input);
	public Parameters initiate();
}
