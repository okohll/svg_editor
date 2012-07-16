package fr.itris.glips.svgeditor.io;

import java.util.concurrent.*;
import fr.itris.glips.svgeditor.io.managers.*;
import fr.itris.glips.svgeditor.io.managers.creation.*;
import fr.itris.glips.svgeditor.io.managers.export.*;

/**
 * the class of the manager enabling to interact with the filesystem
 * @author Jordi SUC
 */
public class IOManager {
	
	/**
	 * the queue of the runnables used to execute io actions
	 */
	private CopyOnWriteArrayList<Runnable> runnablesQueue=
		new CopyOnWriteArrayList<Runnable>();
	
	/**
	 * the "file new" manager
	 */
	private FileNew fileNewManager=new FileNew(this);
	
	/**
	 * the "file open" manager
	 */
	private FileOpen fileOpenManager=new FileOpen(this);
	
	/**
	 * the "file save" manager
	 */
	private FileSave fileSaveManager=new FileSave(this);
	
	/**
	 * the "file close" manager
	 */
	private FileClose fileCloseManager=new FileClose(this);

	/**
	 * the "file print" manager
	 */
	private FilePrint filePrint=new FilePrint(this);
	
	/**
	 * the "file export" manager
	 */
	private FileExport fileExport=new FileExport(this);
	
	/**
	 * the "editor exit" manager
	 */
	private EditorExit editorExitManager=new EditorExit(this);

	/**
	 * the constructor of the class
	 */
	public IOManager(){
		
		Thread thread=new Thread(){
			
			@Override
			public void run() {

				Runnable runnable=null;
				
				while(true){
					
					if(! runnablesQueue.isEmpty()){
						
						//executing the last entered element of the queue
						runnable=runnablesQueue.get(0);
						runnablesQueue.remove(0);
						runnable.run();
						runnable=null;
						
					}else{
						
						//sleeping
						try{sleep(100);}catch (Exception ex){}
					}
				}
			}
		};
		
		thread.start();
	}

	/**
	 * requests this runnable this be executed
	 * @param runnable a runnable
	 */
	public void requestExecution(Runnable runnable){
		
		runnablesQueue.add(runnable);
	}

	/**
	 * @return the "file close" manager
	 */
	public FileClose getFileCloseManager() {
		return fileCloseManager;
	}

	/**
	 * @return the "file new" manager
	 */
	public FileNew getFileNewManager() {
		return fileNewManager;
	}

	/**
	 * @return the "file open" manager
	 */
	public FileOpen getFileOpenManager() {
		return fileOpenManager;
	}

	/**
	 * @return the "file save" manager
	 */
	public FileSave getFileSaveManager() {
		return fileSaveManager;
	}
	
	/**
	 * @return the "file print" manager
	 */
	public FilePrint getFilePrint() {
		return filePrint;
	}
	
	/**
	 * @return the "file export" manager
	 */
	public FileExport getFileExportManager() {
		return fileExport;
	}

	/**
	 * @return the "editor exit" manager
	 */
	public EditorExit getEditorExitManager() {
		return editorExitManager;
	}
}
