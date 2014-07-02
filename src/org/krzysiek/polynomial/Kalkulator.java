package org.krzysiek.polynomial;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class Kalkulator extends MIDlet implements CommandListener {

	private Form mainForm;
	private Form chartForm;
	private PolynomialChart pc = null;
	private Display display;
	
	private Command cancel = new Command("Cancel", Command.CANCEL, 2);
    private Command addCommand = new Command("Dodaj (P1+P2)", Command.OK, 2);
    private Command subCommand = new Command("Odejmij (P1-P2)", Command.OK, 2);
    private Command mulCommand = new Command("Pomnó¿ (P1*P2)", Command.OK, 2);
    private Command divCommand = new Command("Podziel (P1/P2)", Command.OK, 2);

    private Command plot1Command = new Command("Wykres P1", Command.OK, 2);
    private Command plot2Command = new Command("Wykres P2", Command.OK, 2);
    private Command plot3Command = new Command("Wykres wyniku", Command.OK, 2);
    private Command plot4Command = new Command("Wykres reszty", Command.OK, 2);
    
    private TextField polyTxtField1 = new TextField("P1 =", "", 30, TextField.ANY);
    private TextField polyTxtField2 = new TextField("P2 =", "", 30, TextField.ANY);
    private TextBox resultTxtField = new TextBox("", "", 256, 0);
    
    public static Command getBack = new Command("Powrót", Command.CANCEL, 2);
    
    Polynomial[] divResult = null;
    
	public Kalkulator() {
		mainForm = new Form("Kalkulator wielomianów");
		chartForm = new Form("");
		mainForm.append(polyTxtField1);
		mainForm.append(polyTxtField2);
		mainForm.addCommand(cancel);
		mainForm.addCommand(addCommand);
		mainForm.addCommand(subCommand);
		mainForm.addCommand(mulCommand);
		mainForm.addCommand(divCommand);
		mainForm.addCommand(plot1Command);
		mainForm.addCommand(plot2Command);
        //mainForm.addCommand(plot3Command);
        mainForm.setCommandListener(this); 
        
        resultTxtField.addCommand(getBack);
        resultTxtField.addCommand(plot3Command);
        resultTxtField.addCommand(plot4Command);
        resultTxtField.setCommandListener(this);
	}
	
	public Form getMainForm() {
		return mainForm;
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
	    polyTxtField1.delete(0, polyTxtField1.size());
    	polyTxtField2.delete(0, polyTxtField2.size());
    	resultTxtField.delete(0, resultTxtField.size());
	}

	protected void pauseApp() {
		// TODO Auto-generated method stub

	}

	protected void startApp() throws MIDletStateChangeException {
		Display.getDisplay(this).setCurrent(mainForm);    	
    	display = Display.getDisplay(this);
    	display.setCurrent(mainForm);
	}

	public void commandAction(Command c, Displayable d) {
		if(c == cancel) {
			if(d == mainForm) {
	            notifyDestroyed();
	        }
	        else {
	            Display.getDisplay(this).setCurrent(mainForm);
	        }
	    }
	    
	    if(c == addCommand){
	    	Polynomial p1 = new Polynomial(polyTxtField1.getString());
	    	Polynomial p2 = new Polynomial(polyTxtField2.getString());
	    	String res = p1.add(p2).toString();
	    	int length = res.length();
	    	resultTxtField.setMaxSize(length);
	    	resultTxtField.setChars(res.toCharArray(), 0, length);
	    	Display.getDisplay(this).setCurrent(resultTxtField);
	    }
	   
	    if(c == subCommand){
	    	Polynomial p1 = new Polynomial(polyTxtField1.getString());
	    	Polynomial p2 = new Polynomial(polyTxtField2.getString());
	    	String res = p1.subtract(p2).toString();
	    	int length = res.length();
	    	resultTxtField.setMaxSize(length);
	    	resultTxtField.setChars(res.toCharArray(), 0, length);
	    	Display.getDisplay(this).setCurrent(resultTxtField);
	    }
	    
	    if(c == mulCommand){
	    	Polynomial p1 = new Polynomial(polyTxtField1.getString());
	    	Polynomial p2 = new Polynomial(polyTxtField2.getString());
	    	String res = p1.multiply(p2).toString();
	    	int length = res.length();
	    	resultTxtField.setMaxSize(length);
	    	resultTxtField.setChars(res.toCharArray(), 0, length);
	    	Display.getDisplay(this).setCurrent(resultTxtField);
	    }
	    
	    if(c == divCommand){
	    	Polynomial p1 = new Polynomial(polyTxtField1.getString());
	    	Polynomial p2 = new Polynomial(polyTxtField2.getString());
	    	
	    	String res;
	    	
	    	if(p2.coefficient(0) == 0){
	    		res = "B³¹d dzielenia przez 0!";
	    	}
	    	if(p2.degree() == 0){
	    		res = "P2 jest stopnia 0!";
	    	}
	    	else{	    	
		    			    			    	
		    	System.out.println(p2.degree()+", "+p1.degree());
		    	if(p2.degree() < p1.degree())
		    	{		    	
		    		divResult = p1.divideWithRemainder(p2);
		    		res = divResult[0].toString();
			    	String rem = divResult[1].toString();
			    	if(rem != "0"){
			    		res += " reszta: " + rem;
			    	}
		    	}else{
		    		res = "Wielomian P2 jest wiêkszego lub tego samego stopnia co P1!";
		    	}
	    	}
	    	int length = res.length();
	    	resultTxtField.setMaxSize(length);
	    	resultTxtField.setChars(res.toCharArray(), 0, length);
	
	    	Display.getDisplay(this).setCurrent(resultTxtField);
	    	
	    }
	    
	    if(c == plot1Command){
	    	pc = new PolynomialChart(this, chartForm.getWidth(), chartForm.getHeight(), 
	    			polyTxtField1.getString(), -30, 30, 0.1, 20.0, 0.1);
	    	pc.addCommand(getBack);
	    	Display.getDisplay(this).setCurrent(pc); 
	    	
	    }
	    
	    if(c == plot2Command){
	    	pc = new PolynomialChart(this, chartForm.getWidth(), chartForm.getHeight(), 
	    			polyTxtField2.getString(), -30, 30, 0.1, 20.0, 0.1);
	    	pc.addCommand(getBack);
	    	Display.getDisplay(this).setCurrent(pc);
	    }
	    
	    if(c == plot3Command){
	    	if(divResult != null){
	    		if(divResult[0] != null){
			    	pc = new PolynomialChart(this, chartForm.getWidth(), chartForm.getHeight(), 
			    			divResult[0].coefsToString(), -30, 30, 0.1, 20.0, 0.1);
			    	pc.addCommand(getBack);
			    	Display.getDisplay(this).setCurrent(pc);
		    	}
	    	}
	    }
	    
	    if(c == plot4Command){
	    	if(divResult != null){
	    		if(divResult[1] != null){
			    	pc = new PolynomialChart(this, chartForm.getWidth(), chartForm.getHeight(), 
			    			divResult[1].coefsToString(), -30, 30, 0.1, 20.0, 0.1);
			    	pc.addCommand(getBack);
			    	Display.getDisplay(this).setCurrent(pc);
		    	}
	    	}
	    }
	    
	    if(c == getBack){
	    	display = Display.getDisplay(this);
	    	display.setCurrent(mainForm);
	    }
	    
	    if(pc != null){
	    	if(c == pc.zoomInXCommand)pc.zoomInX();
	    	if(c == pc.zoomOutXCommand)pc.zoomOutX();
	    	if(c == pc.zoomInYCommand)pc.zoomInY();
	    	if(c == pc.zoomOutYCommand)pc.zoomOutY();		    
	    }
	    
		
	}

}
