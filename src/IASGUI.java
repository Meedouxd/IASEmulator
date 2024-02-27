import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class IASGUI extends JFrame implements ActionListener {

    IAS ias;
    BufferedReader bf;
    BufferedReader stepper;
    String stepper_line = "";
    JFrame window;
    JLabel nameLabel;
    JLabel accLabel = new JLabel("Accumulator Register");
    JLabel mqLabel = new JLabel("MQ Register");
    JLabel instructionLabel = new JLabel("Current Instruction");

    JTextField accTField = new JTextField();
    JTextField mqTField = new JTextField();
    JTextField instructionTField = new JTextField();

    JButton runButton = new JButton("Run");
    JButton resetButton = new JButton("Reset");
    JButton stepButton = new JButton("Step");


    public IASGUI() throws FileNotFoundException {
        ias = new IAS();
        bf = new BufferedReader(new FileReader("program.ias"));
        stepper = new BufferedReader(new FileReader("program.ias"));
        window = new JFrame();
        window.setBounds(100, 100, 1000, 600);
        window.setVisible(true);
        window.setTitle("IAS Emulator");
        window.setResizable(true);
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(600, 300);
        window.show();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        window.add(accLabel);
        window.add(mqLabel);
        window.add(instructionLabel);


        window.add(accTField);
        window.add(mqTField);
        window.add(instructionTField);

        //window.add(runButton);
        window.add(stepButton);
        window.add(resetButton);
        window.setResizable(false);
        //window.add(stepButton);

        //window.add(runButton);

        //runButton.setBounds(75,50,200,25);


        accLabel.setBounds(65,170,150,25);
        mqLabel.setBounds(250,170,150, 25);
        instructionLabel.setBounds(400, 170,150,25);

        accTField.setBounds(50,200,150,25);
        mqTField.setBounds(212,200,150,25);
        instructionTField.setBounds(375,200,150,25);
        accTField.setEditable(false);
        mqTField.setEditable(false);
        instructionTField.setEditable(false);

       // runButton.setBounds(50,50,75,25);
        stepButton.setBounds(150,50,75,25);
        resetButton.setBounds(250, 50, 75,25);


        runButton.addActionListener(this);
        resetButton.addActionListener(this);
        stepButton.addActionListener(this);

        //validating each instruction
        String line = null;

        try {
            line = bf.readLine();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        while(line != null){
            if(!ias.isValidInstruction(line)){
                JOptionPane.showMessageDialog(null, "Following Line is invalid: " + line, "Error" , JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }else{
                ias.instructionsList.add(line);
            }
            try {
                line = bf.readLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        ias.currentInstructionIndex = 0;
        updateRegisters(ias.instructionsList.get(ias.currentInstructionIndex));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == runButton){
            System.out.println("RUN BUTTON");
        }else if(e.getSource() == resetButton){
            ias.Memory.clear();
            ias.Accumulator = 0;
            ias.MQ = 0;
            ias.doneFlag = false;
            ias.currentInstructionIndex = 0;
            updateRegisters(ias.instructionsList.get(ias.currentInstructionIndex));
        }else if(e.getSource() == stepButton){
            if(ias.doneFlag){
                updateRegisters("DONE");
                JOptionPane.showMessageDialog(null, "Program has ran successfully", "Info" , JOptionPane.INFORMATION_MESSAGE);
            }else{
                ias.runInstruction(ias.instructionsList.get(ias.currentInstructionIndex));
                if(!ias.doneFlag){
                    updateRegisters(ias.instructionsList.get(ias.currentInstructionIndex));
                }else{
                    updateRegisters("DONE");
                    JOptionPane.showMessageDialog(null, "Program has ran successfully", "Info" , JOptionPane.INFORMATION_MESSAGE);
                }
            }

        }
    }
    public void updateRegisters(String instruction){
        accTField.setText(String.valueOf(ias.Accumulator));
        mqTField.setText(String.valueOf(ias.MQ));
        instructionTField.setText(instruction);
    }
}
