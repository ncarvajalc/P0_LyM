package uniandes.lym.robot.control;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.HashMap;

import javax.swing.SwingUtilities;

import uniandes.lym.robot.kernel.*;



/**
 * Receives commands and relays them to the Robot. 
 */

public class Interpreter   {
	
	/**
	 * Robot's world
	 */
	private RobotWorldDec world;   
	
	
	public Interpreter()
	  {
	  }


    /**
	 * Creates a new interpreter for a given world
	 * @param world 
	 */


	public Interpreter(RobotWorld mundo)
      {
		this.world =  (RobotWorldDec) mundo;
		
	  }
	
	
	/**
	 * sets a the world
	 * @param world 
	 */

	public void setWorld(RobotWorld m) 
	{
		world = (RobotWorldDec) m;
		
	}
	  
	
	
	/**
	 *  Processes a sequence of commands. A command is a letter  followed by a ";"
	 *  The command can be:
	 *  M:  moves forward
	 *  R:  turns right
	 *  
	 * @param input Contiene una cadena de texto enviada para ser interpretada
	 */
	
	public String process1(String input) throws Error
     {   
		
		 
		StringBuffer output=new StringBuffer("SYSTEM RESPONSE: -->\n");	
		
		int i;
	   int n;
	   boolean ok = true;
	   n= input.length();
	    
	    i  = 0;
	    try	    {
	    while (i < n &&  ok) {
	    	switch (input.charAt(i)) {
	    		case 'M': world.moveForward(1); output.append("move \n");break;
	    		case 'R': world.turnRight(); output.append("turnRignt \n");break;
	    		case 'C': world.putChips(1); output.append("putChip \n");break;
	    		case 'B': world.putBalloons(1); output.append("putBalloon \n");break;
	    		case  'c': world.pickChips(1); output.append("getChip \n");break;
	    		case  'b': world.grabBalloons(1); output.append("getBalloon \n");break;
	    		default: output.append(" Unrecognized command:  "+ input.charAt(i)); ok=false;
	          }
	
	    	  if (ok) {
	    		 if  (i+1 == n)  { output.append("expected ';' ; found end of input; ");  ok = false ;}
	    		 else if (input.charAt(i+1) == ';') 
	    		 {
	    			 i= i+2;
	    			 try {
	    			        Thread.sleep(1000);
	    			    } catch (InterruptedException e) {
	    			        System.err.format("IOException: %s%n", e);
	    			    }
	    			 
	    		 }
	    		 else {output.append(" Expecting ;  found: "+ input.charAt(i+1)); ok=false;
	    	 }
	    	 }
	    
	    
	    }
	    
	    }
	 catch (Error e ){
	 output.append("Error!!!  "+e.getMessage());
	 }
	 
 
	    return output.toString();
	    }

	
	
	
	
	
	
	public String process(String input) throws Error
	{
		
		StringBuffer output=new StringBuffer("SYSTEM RESPONSE: -->\n");	
		
		HashMap map = new HashMap();
		String[] inst = input.split("\n");
		for (int i = 0; i < inst.length; i++) {
		String entrada = inst[i];
		try {
		
		if(entrada.startsWith("VARS"));
		{
			
			String cadena = entrada.substring(4);
			String[] variables = cadena.split(",");
			for (int j = 0; j < variables.length; j++) {
				map.put(variables[j], 0);
			}
			output.append("VARS \n");
		}
		if(entrada.startsWith("assing"))
		{
			
			String cadena = entrada.substring(7, 10);
			String[] parametros = cadena.split(",");
			for (int j = 0; j < map.size(); j++) {
				if(map.containsKey(parametros[0]))
				{
					map.replace(parametros[0], 0, parametros[1]);
				}
			}
				
		}
		 if (entrada.startsWith("move"))
		 {
			 
			 boolean variableMap = false;
			 char numero = entrada.charAt(5);
			 int numMovimientos = 0;
			 for (int j = 0; j < map.size(); j++) {
					if(map.containsKey(numero))
					{
						numMovimientos = (int) map.get(numero);
						world.moveForward(numMovimientos);
						variableMap = true; 
					}
					
		 }
			 if(!variableMap)
			 {
				 numMovimientos = (int) numero; 
				 world.moveForward(numMovimientos);
			 }
			 
			 
			 
			
	 
		}
		 else if(entrada.startsWith("turn"))
		 {
			 output.append("turn");
			 String parametro = entrada.substring(5);
			 if(parametro.startsWith("right"))
			 {
				 world.turnRight();
			 }
			 else if(parametro.startsWith("left"))
			 {
				 world.turnRight();
				 world.turnRight();
				 world.turnRight();
			 }
			 else {
				 world.turnRight();
				 world.turnRight();
			 }
			 
		 }
		 else if(entrada.startsWith("face"))
		 {
			 String parametro = entrada.substring(5);
			 int orient2;
			 boolean encontro = false;
			 if(parametro.startsWith("north"))
			 {
				 orient2 = 0;
			 }
			 else if(parametro.startsWith("south"))
			 {
				 orient2 = 1;
			 }
			 else if(parametro.startsWith("east"))
			 {
				 orient2 = 2;
			 }
			 else {
				 orient2 = 3;
			 }
			 int orient = world.getFacing();
			 while(!encontro)
			 {
				 if(orient == orient2);
				 {
					 encontro = true;
				 } 
					 world.turnRight();
					
				 
			 }
			 
			 
			 
			 
			 
		 }
		
		}
		catch(Error e ){
			 output.append("Error!!!  "+e.getMessage());
		 }
		
		
		
		
		
		
		
		}
		
		
		
		return output.toString();
		
	}
		

	
}


	    