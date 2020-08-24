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



	public String process(String input) throws Error
	{

		StringBuffer output=new StringBuffer("SYSTEM RESPONSE: -->\n");	

		HashMap<String, Integer> map = new HashMap<String, Integer> ();
		String[] inst = input.split("\n");
		if(input.contains("ROBOT_R") && input.contains("BEGIN")&& input.contains("END") )
		{	
			for (int i = 0; i < inst.length; i++) {
				String entrada = inst[i];

				try {


					if(entrada.startsWith("VARS"))
					{

						String cadena = entrada.substring(5);
						cadena = cadena.replace(" ", "");
						String[] variables = cadena.split(",");
						for (int j = 0; j < variables.length; j++) {
							map.put(variables[j], 0);
						}

					}
					else if(entrada.startsWith("assign"))
					{

						String cadena = entrada.substring(7);
						cadena = cadena.replace(");", "");
						String[] parametros = cadena.split(",");
						int valor = Integer.parseInt(parametros[1]);
						if(map.containsKey(parametros[0]))
						{
							map.replace(parametros[0], valor);
						}
					}
					else if (entrada.startsWith("move") && !entrada.contains("moveDir") && !entrada.contains("moveInDir"))
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
						else if (parametro.startsWith("around")){
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
						String parametro = parametroConParentesis.replace(");", "");
						String[] tokens = parametro.split(",");

						String objeto = tokens[1].replace(" ", "");;
						System.out.println(objeto);
						String nStr = tokens[0].replace(" ", "");;
						System.out.println(nStr);
						//CASO CHIPS
						if(objeto.equals("chips"))
						{
							int n = 0;
							if(map.containsKey(nStr))
							{
								n = map.get(nStr);
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
							if(map.containsKey(nStr))
							{
								n = map.get(nStr);
								world.putBalloons(n);
								System.out.println("h");
							}
							else {
								n = Integer.parseInt(nStr);
								world.putBalloons(n);
							}


						}
					}
					else if(entrada.startsWith("pick")) {
						String parametroConParentesis = entrada.substring(5);
						String parametro = parametroConParentesis.replace(");", "");
						String[] tokens = parametro.split(",");

						String objeto = tokens[1].replace(" ", "");;
						String nStr = tokens[0].replace(" ", "");;
						//CASO CHIPS
						if(objeto.equals("chips"))
						{
							int n = 0;
							if(map.containsKey(nStr))
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
							if(map.containsKey(nStr))
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
						String parametro = parametroConParentesis.replace(");", "");
						String[] tokens = parametro.split(",");

						String direccion = tokens[1].replace(" ", "");;
						String nStr = tokens[0].replace(" ", "");;

						int posicionActual = world.getFacing();

						int n = 0;

						//CASO NORTH
						if(posicionActual== RobotWorldDec.NORTH)
						{
							if(map.containsKey(nStr))
							{
								n = map.get(nStr);

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
							if(map.containsKey(nStr))
							{
								n = map.get(nStr);

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
							if(map.containsKey(nStr))
							{
								n = map.get(nStr);

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
							if(map.containsKey(nStr))
							{
								n = map.get(nStr);

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

						String pasos = tokens[0].replace(" ", "");;
						String parametro = tokens[1].replace(" ", "");;

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
						if(map.containsKey(pasos))
						{
							n = map.get(pasos);

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

							String parametro = parametroConParentesis.replace(");", "");

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
							parametro = parametro.replace(";", "");

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

		} else {
			output.append("No routine detected");
		}
		return output.toString();


	}



}


