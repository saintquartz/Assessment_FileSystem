import java.util.*;

class File {
    private String fileName;
    private int fileSize;

    public File(String name, int size) {
        fileName = name;
        fileSize = size;
    }

    public String getFileName() {
        return fileName;
    }

    public int getFileSize() {
        return fileSize;
    }
}

class Directory {
    private String directoryName;
    private Map<String, File> directoryFiles = new HashMap<>(); //key - name of file, value - File object 
    private Directory directoryParent;
    private Map<String, Directory> subDirectories = new HashMap<>(); //key - name of subdirectoy/folder, value - subdirectory object 

    public Directory(String name, Directory parent) {
        directoryName = name;
        directoryParent = parent;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public Directory getDirectoryParent() {
        return directoryParent;
    }

    public Directory getSubDirectory(String name) {
        return subDirectories.get(name);
    }

    public void directoryAddFile(String name, int size) {
        directoryFiles.put(name, new File(name, size));
    }

    public void directoryDeleteFile(String name) {
        directoryFiles.remove(name);
    }

    public void directoryAddFolder(String name) {
        subDirectories.put(name, new Directory(name, this));
    }

    public void directoryDeleteFolder(String name) {
        subDirectories.remove(name);
    } 

    public int getDirectorySize() {
        int size = directoryFiles.values().stream().mapToInt(File::getFileSize).sum();

        for (Directory subDirect : subDirectories.values()) {
            size += subDirect.getDirectorySize();
        }
        
        return size;
    }

    public void listDirectory() {
        for (File file : directoryFiles.values()) {
            System.out.println(file.getFileName());
        }

        for (String subName : subDirectories.keySet()) {
            System.out.println(subName + "/");
        }
    }
    
}

class FileSystem {
    private Directory root;
    private Directory current;

    public FileSystem() {
        root = new Directory("root", null);
        current = root;
    }

    public void cd(String name) {
        if (name.equals("..")) {
            if (current.getDirectoryParent() != null) {
                current = current.getDirectoryParent();
            }
        } else {
            if (current.getSubDirectory(name) != null) {
                current = current.getSubDirectory(name);
            } else {
                System.out.println("Directory does not exist");
            }
        }
    }

    public void ls() {
        current.listDirectory();
    }

    public void size() {
        System.out.println("Size: " + current.getDirectorySize());
    }

    public void createFile(String name, int size) {
        current.directoryAddFile(name, size);
    }

    public void createFolder(String name) {
        current.directoryAddFolder(name);
    }

    public void deleteFile(String name) {
        current.directoryDeleteFile(name);
    }

    public void deleteFolder(String name) {
        current.directoryDeleteFolder(name);
    }

}

class App {
    private FileSystem simulator;

    public App() {
        simulator = new FileSystem();
    }

    public void commands() {
        System.out.println("Commands: ");
        System.out.println("cd [file/folder/..] - Change directory location");
        System.out.println("ls - List all files/folders in current directory");
        System.out.println("size - Return total size of current directory");
        System.out.println("createfile [name] [size] - Creates file of name with size in current directory");
        System.out.println("createfolder [name] - Creates folder of name in current directory");
        System.out.println("delete [f/d] [name] - Deletes file(f) or folder(d) of name in current directory");
        System.out.println("help - Displays available commands");
        System.out.println("exit - Closes application");
    }

    public void start() {
        commands();
        Scanner s = new Scanner(System.in);

        while (true) {
            System.out.println();
            String input = s.nextLine();
            String[] splitInput = input.split("\\s+");
        
            if (splitInput.length == 0) {
                continue;
            } 

            if (splitInput[0].equals("cd")) {
                if (splitInput.length == 2) {
                    simulator.cd(splitInput[1]);
                } else {
                    simulator.cd(splitInput[0]);
                }
            } else if (splitInput[0].equals("ls")) {
                simulator.ls();
            } else if (splitInput[0].equals("size")) {
                simulator.size();
            } else if (splitInput[0].equals("createfile")) {
                if (splitInput.length == 3) {
                    try {
                        int size = Integer.parseInt(splitInput[2]);
                        simulator.createFile(splitInput[1], size);
                    } catch (NumberFormatException e) {
                        System.out.println("invalid format");
                    }
                }
            } else if (splitInput[0].equals("createfolder")) {
                if (splitInput.length == 2) {
                    simulator.createFolder(splitInput[1]);
                }
            } else if (splitInput[0].equals("delete")) {
                if (splitInput.length == 3) {
                    if (splitInput[1].equals("f")) {
                        simulator.deleteFile(splitInput[2]);
                    } else if (splitInput[1].equals("d")) {
                        simulator.deleteFolder(splitInput[2]);
                    }
                }
                
            } else if (splitInput[0].equals("exit")) {
                return;
            } else if (splitInput[0].equals("help")) {
                commands();
            }
        }
    }

}

class Main {
    public static void main(String[] args) throws Exception {
        new App().start();
    }
}
