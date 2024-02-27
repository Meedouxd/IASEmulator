import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IAS {
    public static final String OPCODES[] = {"LOAD", "STOR", "ADD", "SUB", "MUL", "DIV", "LSH", "RSH", "JUMP"};
    public  Map<String, String> Memory;
    double Accumulator = 0;
    double MQ = 0;

    boolean doneFlag = false;
    ArrayList<String> instructionsList;
    int currentInstructionIndex;
    public IAS(){
        //                   line #,  instruction
        Memory = new HashMap<String, String>();

        instructionsList = new ArrayList<String>();
    }
    public void runInstruction(String instruction){
        boolean absFlag = false;
        boolean negFlag = false;
        if(instruction.contains("|")){
            absFlag = true;
        }
        if(instruction.contains("-")){
            negFlag = true;
        }
        String contents[] = instruction.split(" ");
        String memLocation;
        if(currentInstructionIndex == instructionsList.size()-1){
            doneFlag = true;
        }
        switch(contents[1]){
            case("LOAD"):
                if(contents[2].equals("MQ")){
                    MQ = Accumulator;
                    currentInstructionIndex++;
                    break;
                    //LOAD MQ,MX(N)
                }if(contents[2].contains("MQ")){
                    //memLocation = contents[2].substring(6, contents[2].length()-1);
                    memLocation = digitsOnly(contents[2]);
                    MQ = Double.parseDouble(Memory.get(memLocation));
                    currentInstructionIndex++;
                    break;
                }
                //memLocation = contents[2].substring(2, contents[2].length()-1);
                memLocation = digitsOnly(contents[2]);
                Accumulator = Double.parseDouble(Memory.get(memLocation));
                if(absFlag){
                    Accumulator = Math.abs(Accumulator);
                }
                if(negFlag){
                    Accumulator *= -1;
                }
                currentInstructionIndex++;
                break;
            case("STOR"):
                //memLocation = contents[2].substring(2, contents[2].length()-1);
                memLocation = digitsOnly(contents[2]);
                Memory.put(memLocation, Double.toString(Accumulator));
                currentInstructionIndex++;
                break;
            case("ADD"):
                //memLocation = contents[2].substring(2, contents[2].length()-1);
                memLocation = digitsOnly(contents[2]);
                double addTo = Double.parseDouble(Memory.get(memLocation));
                if(absFlag){
                    Accumulator += Math.abs(addTo);
                }else{
                    Accumulator += addTo;
                }
                currentInstructionIndex++;
                break;
            case("SUB"):
                //memLocation = contents[2].substring(2, contents[2].length()-1);
                memLocation = digitsOnly(contents[2]);
                double subTo = Double.parseDouble(Memory.get(memLocation));
                if(absFlag){
                    Accumulator -= Math.abs(subTo);
                }else{
                    Accumulator -= subTo;
                }
                currentInstructionIndex++;
                break;
            case("MUL"):
                //memLocation = contents[2].substring(2, contents[2].length()-1);
                memLocation = digitsOnly(contents[2]);
                MQ *= Double.parseDouble(Memory.get(memLocation));
                Accumulator = MQ;
                currentInstructionIndex++;
                break;
            case("DIV"):
                //memLocation = contents[2].substring(2, contents[2].length()-1);
                memLocation = digitsOnly(contents[2]);
                Accumulator /= Double.parseDouble(memLocation);
                MQ = Accumulator % Double.parseDouble(memLocation);
                currentInstructionIndex++;
                break;
            case("LSH"):
                Accumulator *=2;
                currentInstructionIndex++;
                break;
            case("RSH"):
                Accumulator /=2;
                currentInstructionIndex++;
                break;
            case("JUMP"):
                if(contents.length == 4){
                    if(Accumulator > -1){
                        memLocation = contents[3].substring(2, contents[3].length()-1);
                        System.out.println("Mem Location: " + memLocation);
                        for(int i = 0; i < instructionsList.size(); i++){
                            String s_array[] = instructionsList.get(i).split(" ");
                            if(s_array[0].equals(memLocation)){
                                currentInstructionIndex = i;
                                doneFlag = false;
                                break;
                            }
                        }
                    }else{
                        currentInstructionIndex++;
                    }
                    break;
                }
                memLocation = contents[2].substring(2, contents[2].length()-1);
                System.out.println("Mem Location: " + memLocation);
                for(int i = 0; i < instructionsList.size(); i++){
                    String s_array[] = instructionsList.get(i).split(" ");
                    if(s_array[0].equals(memLocation)){
                        currentInstructionIndex = i;
                        doneFlag = false;
                        break;
                    }
                }
                break;
                // if is not instruction, it is variable!
            default:
                Memory.put(contents[0], contents[1]);
                currentInstructionIndex++;
                break;
        }
    }
    boolean isValidInstruction(String instruction){
        if(!instruction.contains(" ")){
            return false;
        }
        boolean isValidInstruction = false;
        String contents[] = instruction.split(" ");
        if(contents.length == 2){
            try{
                double d = Double.parseDouble(contents[1]);
            }catch(NumberFormatException n){
                isValidInstruction = false;
                return false;
            }
            isValidInstruction = true;
            return true;
        }
        for(String i : OPCODES){
            if(contents[1].equals(i)){
                isValidInstruction = true;
            }
        }
        return isValidInstruction;
    }
    public String digitsOnly(String whatever){
        String toReturn = "";
        char digits[] = {'0','1','2','3','4','5','6','7','8','9'};
        for(int i = 0; i < whatever.length(); i++){
            for(int c = 0; c < digits.length; c++){
                if(whatever.charAt(i) == digits[c]){
                    toReturn = toReturn + digits[c];
                }
            }
        }
        return toReturn;
    }
}
