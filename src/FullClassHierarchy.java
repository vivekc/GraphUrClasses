import java.util.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.*;
public class FullClassHierarchy {
	
	static String fileName="output.dot" ; 
	private static void checkInterfaces(Class currentClass, BufferedWriter bfw){
		if(currentClass != null){
			Class []Interfaces = currentClass.getInterfaces();
			
			for(Class anInterface:Interfaces){
				try{
					bfw.write("\t node[shape=\"ellipse\",peripheries=2,style=\"filled\",color=\".3 .3 .5\"];");
					bfw.write("\"" + anInterface.getName()+"\"->\"" + currentClass.getName() + "\"[style=\"dotted\"];\n");
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	private static void checkClass(Class currentClass, BufferedWriter bfw){
		if(currentClass!=null){
			Class superClass= currentClass.getSuperclass();
			checkClass(superClass,bfw);
			
			Method []Methods = currentClass.getDeclaredMethods();
			
			if(superClass!=null){
				try{
					bfw.write("\"" + superClass.getName()+"\"->\""+currentClass.getName()+"\";\n");
					for(Method aMethod:Methods){
						bfw.write("\"" + currentClass.getName()+ "\"->\""+aMethod.getName()+"\";\n");				
					}
				}
				catch(IOException e) {
					e.printStackTrace();
				}
			}
			checkInterfaces(currentClass,bfw);
		}
	}
	public static void main(String []args){
		if(args.length !=1){
			System.exit(-1);
		}
		Class currentClass=null;
		try{
			currentClass = Class.forName(args[0]);
		}catch(ClassNotFoundException e){
			System.out.println("enter fully qualified name of the class");
			System.exit(-2);
		}
		
		File f = new File(fileName);
		try {
			BufferedWriter bfw = new BufferedWriter(new FileWriter(f));
			bfw.write("digraph G {");
			bfw.write("\t node[shape=\"box\"];");
			bfw.write("\t edge[arrowtail=\"empty\",dir=\"back\"];");
			checkClass(currentClass,bfw);
			bfw.write("}");
			bfw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			String output = args[0]+".png";
			String command1 = "/usr/bin/dot -Tpng "+ fileName +" -o "+ output;
			System.out.println(command1);
			Runtime.getRuntime().exec(command1);
			String command2 = "/usr/bin/gthumb "+ output +" &";
			Runtime.getRuntime().exec(command2);
			System.out.println(command2);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
