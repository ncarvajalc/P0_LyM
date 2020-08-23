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

		HashMap<String, Integer> map = new HashMap<String, Integer> ();
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

				}
				if(entrada.startsWith("assing"))
				{

					String cadena = entrada.substring(7, 10);
					String[] parametros = cadena.split(",");
					int valor = Integer.parseInt(parametros[1]);
					if(map.containsKey(parametros[0]))
					{
						map.replace(parametros[0], valor);
					}


				}
				if (entrada.startsWith("move") && !entrada.contains("moveDir") && !entrada.contains("moveInDir"))
				{


					char numero = entrada.charAt(5);
					int numMovimientos = 0;
					if(map.containsKey(String.valueOf(numero)))
					{
						numMovimientos =  map.get(String.valueOf(numero));
						world.moveForward(numMovimientos);
					}
					else {

						numMovimientos = Character.getNumericValue(numero);
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
					int orient2= 0;
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
						if(orient!= orient2)
						{
							world.turnRight();
							orient = world.getFacing();
						}
						else {
							encontro = true;
						}

					}





				}
				else if(entrada.startsWith("put")) {
					String parametroConParentesis = entrada.substring(4);
					String parametro = parametroConParentesis.replace(")", "");
					String[] tokens = parametro.split(",");

					String objeto = tokens[1];
					System.out.println(objeto);
					String nStr = tokens[0];
					System.out.println(nStr);
					//CASO CHIPS
					if(objeto.equals("chips"))
					{
						int n = 0;
						if(false)
						{
							n = (int) map.get(nStr);
							world.putChips(n);
						}

						else {
							n = Integer.parseInt(nStr);
							world.putChips(n);
						}


					}

					//CASO BALLOONS
					if(objeto.equals("balloons"))
					{
						int n = 0;
						if(false)
						{
							n = (int) map.get(nStr);
							world.putBalloons(n);
						}
						else {
							n = Integer.parseInt(nStr);
							world.putBalloons(n);
						}


					}
				}
				else if(entrada.startsWith("pick")) {
					String parametroConParentesis = entrada.substring(5);
					String parametro = parametroConParentesis.replace(")", "");
					String[] tokens = parametro.split(",");

					String objeto = tokens[1];
					String nStr = tokens[0];
					//CASO CHIPS
					if(objeto.equals("chips"))
					{
						int n = 0;
						if(false)
						{
							n = (int) map.get(nStr);
							world.pickChips(n);
						}
						else {
							n = Integer.parseInt(nStr);
							world.pickChips(n);
						}


					}

					//CASO BALLOONS
					if(objeto.equals("balloons"))
					{
						int n = 0;
						if(false)
						{
							n = (int) map.get(nStr);
							world.grabBalloons(n);
						}
						else {
							n = Integer.parseInt(nStr);
							world.grabBalloons(n);
						}
					}
				}

				else if(entrada.startsWith("moveDir")) {
					String parametroConParentesis = entrada.substring(8);
					String parametro = parametroConParentesis.replace(")", "");
					String[] tokens = parametro.split(",");

					String direccion = tokens[1];
					String nStr = tokens[0];

					int posicionActual = world.getFacing();

					int n = 0;

					//CASO NORTH
					if(posicionActual== RobotWorldDec.NORTH)
					{
						if(false)
						{

						}
						else {
							n = Integer.parseInt(nStr);
						}

						System.out.println(n);


						if(direccion.equals("front"))
						{
							world.moveVertically(-n);
						}
						else if(direccion.equals("right")) {
							world.moveHorizontally(n);
						}
						else if(direccion.equals("left"))
						{
							world.moveHorizontally(-n);
						}
						else if(direccion.equals("back"))
						{
							world.moveVertically(n);
						}
					}
					//CASO EAST
					if(posicionActual== RobotWorldDec.EAST)
					{
						if(false)
						{

						}
						else {
							n = Integer.parseInt(nStr);
						}


						if(direccion.equals("front"))
						{
							world.moveHorizontally(n);
						}
						else if(direccion.equals("right")) {
							world.moveVertically(n);
						}
						else if(direccion.equals("left"))
						{
							world.moveVertically(-n);
						}
						else if(direccion.equals("back"))
						{
							world.moveHorizontally(-n);
						}
					}

					//CASO SOUTH
					if(posicionActual== RobotWorldDec.SOUTH)
					{
						if(false)
						{

						}
						else {
							n = Integer.parseInt(nStr);
						}


						if(direccion.equals("front"))
						{
							world.moveVertically(n);
						}
						else if(direccion.equals("right")) {
							world.moveHorizontally(-n);
						}
						else if(direccion.equals("left"))
						{
							world.moveHorizontally(n);
						}
						else if(direccion.equals("back"))
						{
							world.moveVertically(-n);
						}
					}
					//CASO WEST
					if(posicionActual== RobotWorldDec.WEST)
					{
						if(false)
						{

						}
						else {
							n = Integer.parseInt(nStr);
						}


						if(direccion.equals("front"))
						{
							world.moveHorizontally(-n);
						}
						else if(direccion.equals("right")) {
							world.moveVertically(-n);
						}
						else if(direccion.equals("left"))
						{
							world.moveVertically(n);
						}
						else if(direccion.equals("back"))
						{
							world.moveHorizontally(n);
						}
					}
				}

				else if(entrada.startsWith("moveInDir"))
				{
					String parametroConParentesis = entrada.substring(10);
					String parametros = parametroConParentesis.replace(")", "");
					String[] tokens = parametros.split(",");

					String pasos = tokens[0];
					String parametro = tokens[1];

					int orient2= 0;
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
						if(orient!= orient2)
						{
							world.turnRight();
							orient = world.getFacing();
						}
						else {
							encontro = true;
						}

					}

					int n = 0;
					if (false) {

					}
					else {
						n = Integer.parseInt(pasos);
						world.moveForward(n);
					}
				}

				else if(entrada.equals("skip"))
				{
					output.append("Skipped");
				}
				else if(entrada.startsWith("facing")|| entrada.startsWith("not"))
				{
					String parametroConParentesis = "";

					if (entrada.startsWith("facing")) {
						parametroConParentesis = entrada.substring(7);

						String parametro = parametroConParentesis.replace(")", "");

						if(parametro.equals("north") && world.facingNorth())
							output.append("true");

						else if(parametro.equals("south") && world.facingSouth())
							output.append("true");

						else if(parametro.equals("east") && world.facingEast())
							output.append("true");

						else if(parametro.equals("west") && world.facingWest())
							output.append("true");
						else {
							output.append("false");
						}
					}
					else if (entrada.startsWith("not")) {
						parametroConParentesis = entrada.substring(11);

						String parametro = parametroConParentesis.replace(")", "");

						if(parametro.equals("north") && world.facingNorth())
							output.append("false");

						else if(parametro.equals("south") && world.facingSouth())
							output.append("false");

						else if(parametro.equals("east") && world.facingEast())
							output.append("false");

						else if(parametro.equals("west") && world.facingWest())
							output.append("false");
						else {
							output.append("true");
						}
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


