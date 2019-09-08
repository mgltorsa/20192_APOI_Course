
import java.util.Scanner;


/**
 * Calculator
 */
public class Calculator {


    /**
     * Main thread, initialize the application
     * 
     * @param args args are unused
     */
    public static void main(String[] args) {

        System.out.println("Welcome to calculator app.\n");


        // Printing app rules.
        printInfo();

        Scanner sc = new Scanner(System.in);


        // when mode is equal to 0, calculator is in single operation mode. however,
        // when is equal to 1 calculator is in multi operation mode.
        int mode = -1;

        String continueLine = "";

        do {
            mode = readMode(sc);

            start(mode, sc);

            System.out.println("Type 1 to continue using the app\n");
            continueLine = sc.nextLine();

        } while (continueLine.equals("1"));

        System.out.println("Good bye!\n");
        sc.close();

    }

    /**
     * Show in window the information about calculator: patterns for the operations and some
     * examples. This method use System class to print the info.
     */
    public static void printInfo() {
        String info = "This calculator works in degrees\n" + "Operations examples:\n"
                + "sum,rest,multiplication,division: {number} {operation} {number} (e.g. 1 + 2 = 3; 1 / 2 = 0.5; 2 * 4 = 8; 4 - 2 = 2)\n"
                + "modulo: {number} {operation} {mod} (e.g. 2 % 4 = 0)\n"
                + "sine: {number} sin (e.g. 0 sin = 0)\n"
                + "cosine: {number} cos (e.g. 0 cos = 1)\n"
                + "tangent: {number} tan (e.g. 0 tan = 0)\n"
                + "10-based logarithm: {number} L (e.g. 1 L = 0)\n"
                + "n-based logarithm: {number} LN {base} (e.g. 1 LN 5 = 0)\n"
                + "square root: {number} R (e.g. 4 R = 4)\n"
                + "n-th root: {number} Rn {n} (e.g. 27 RN 3 = 3)\n"
                + "power: {base} ^ {exp} (e.g. 1 ^ 2 = 1)\n"
                + "10-based number: {cientific notation} tbn (e.g. A tbn = 1,0 x 10^1)\n"
                + "factorial: number ! (e.g. 3 ! = 6)\n"
                + "degree conversion: {number} dg (e.g. PI dg = 180)\n"
                + "radian conversion: {number} rd (e.g. 180 rd = PI)\n"
                + "n-based conversion [to decimal, binary or hexadecimal conversion]: {number} to {base} (e.g. 3 to 2 = 11|2, 11 to 10 = 3|10, 16 to 16 = 1|16)\n"
                + "using memory: mem{position} [position from 0 to 9] (e.g. mem5 [where mem5=2] + 4 = 6)\n"
                + "Type PI number writing PI.\n";
        System.out.println(info);
    }

    /**
     * Reads the mode for calculator from input channel, verifying inputs and doing a loop while
     * user doesn't type a correct value, showing warning or errors messages in console when an
     * incorrect value is typed.
     * 
     * @param sc the input channel of the calculator. sc != null. Input channel must not be closed.
     * @return mode:int, which represents the typed mode by user.
     */
    public static int readMode(Scanner sc) {
        int mode = -1;
        String modeStr = "";
        do {
            System.out.println("Type 0 for single mode or 1 for multi-operation mode\n");
            modeStr = sc.nextLine();
            if (modeStr.isEmpty() || modeStr.trim().length() > 1) {
                System.out.println("Type a valid command\n");
                continue;
            }

            char character = modeStr.charAt(0);

            if (!Character.isDigit(character)) {
                System.out.println("Only type numbers 0 or 1\n");
                continue;
            }

            mode = Integer.parseInt(character + "");

            if (mode != 0 && mode != 1) {
                System.out.println("Type a valid mode\n");
            }

        } while (mode != 0 && mode != 1);
        return mode;
    }


    /**
     * Start the calculator, initializing the memory {Array:int of length 10} and memoryPointers.
     * This methods attends saved memory [save each result in memory, using {@link #saveResult(double[], int, String)} method],
     * printing also the results of each operation. For operation, this method use
     * {@link #readAndOperate(Scanner, double[])} method which returns an String value which contains the operation's result
     * typed by the user. This String must be validated as or error before //TODO:.
     * 
     * @param mode refers to mode of the application: 0 mode refers a single-operation calculator,
     *             but 1 mode to a multi-operation calculator. Start method is in a loop while mode
     *             1 is actived. Mode must be 0 or 1
     * @param sc   the input channel of the calculator. sc != null. Input channel must not be
     *             closed.
     */

    public static void start(int mode, Scanner sc) {
        // memory
        double[] memory = new double[10];
        // Index for last typed result
        int memoryPointer = 0;

        String resultStr = -1 + "";

        // when is mode 1, for correct operation, it 'if' is executed for realized the first
        // operation and so, can operate
        // with the last result (saved in memory).
        if (mode == 1) {

            resultStr = readAndOperate(sc, memory);

            System.out.println("||\n" + resultStr);

            // saving result in memory
            memoryPointer = saveResult(memory, memoryPointer, resultStr);
        }

        do {
            if (mode == 0) {
                resultStr = readAndOperate(sc, memory);
            }

            if (mode == 1) {
                String line = getLastResult(memory, memoryPointer) + "";
                resultStr = doNumberOperation(sc, line, memory);
            }


            System.out.println("||\n" + resultStr);

            // saving result in memory
            memoryPointer = saveResult(memory, memoryPointer, resultStr);



        } while (wait(mode));
    }

    /**
     * Allows to save a real [double] number in memory, inserting the result in the given pointer,
     * And returning the next free space. When there are not more space, the returned pointer must
     * point to init of the memory. This method, also, doesn't save Nan or Errors and converts
     * strResults in double from binary,hexadecimal or decimal conversion or scientist notation
     * (calling {@link #getValueOfScientificNotation(String)})
     * <p>
     * Pre:
     * </p>
     * memory has already been initalized as array of 10 positions.
     * 
     * @param memory        represents the memory of program. Must be an array of doubles. memory !=
     *                      null
     * @param memoryPointer represents the current free space in memory to save a number.
     *                      {@code memoryPointer >= 0 and memoryPointer < memory.length}.
     * @param strResult     represents the result that will be saved in memory. result must not
     *                      empty or null And must not be a calculator's error. And if the parameter
     *                      represents a value of an conversion, must be use one of the patterns
     *                      presented on the calculator's info.
     * @return An int which represents the new pointer which points a current free space in memory.
     */
    public static int saveResult(double[] memory, int memoryPointer, String strResult) {

        if (!strResult.toLowerCase().contains("error")
                && !strResult.toLowerCase().contains("nan")) {

            double result = 0;
            if (strResult.contains("B-")) {
                // info[0] contains the number, and info[1] contains the base of this number.
                String[] info = strResult.split("B-");
                int base = Integer.parseInt(info[1]);
                result = tenBaseConversion(info[0], base);
            } else if (strResult.contains("x")) {

                result = getValueOfScientificNotation(strResult);
            } else {
                result = Double.parseDouble(strResult);
            }
            int newPointer = memoryPointer;
            memory[newPointer++] = result;
            if (newPointer >= memory.length) {
                newPointer = 0;
            }
            return newPointer;

        }

        return memoryPointer;
    }

    /**
     * Return a double from a scientific notation string which use the scientis notation pattern of
     * this calculator ({number}x10to{base}). Using String split function, converts {number} to
     * double and {base} to Integer and multiply {number} by 10 {base} times.
     * 
     * @param notation Represents the number in Scientific notation (with the pattern of this
     *                 calculator app). notation must not be empty or null
     * @return A double value which contains the real value of scientific notation
     */
    public static double getValueOfScientificNotation(String notation) {
        double result = 0;
        String[] info = notation.split("x");
        String strNumber = info[0];

        String infoPow = info[1].split("to")[1];
        int exp = Integer.parseInt(infoPow);
        result = Double.parseDouble(strNumber);
        for (int i = 0; i < exp; i++) {
            result *= 10;
        }
        return result;

    }

    /**
     * Returns true if calculator's mode != 0 or false at instead.
     * 
     * @param mode represents the current calculator's mode. mode must be either 0 or 1.
     * @return A boolean value which represents if the calculators must wait another operation of
     *         the user.
     */
    public static boolean wait(int mode) {
        return mode != 0;
    }

    /**
     * Returns the last number saved in memory. If it is in 0-indexed space, then the returned value
     * will be the last-indexed number in memory, so, the number at the last index [memory.length-1]
     * will be returned.
     * 
     * @param memory        represents the memory of calculator. memory != null and memory must be
     *                      an array of doubles
     * @param memoryPointer represents the current pointer to free space in memory. {@code memoryPointer >= 0}
     *                      and {@code memoryPointer < memory.length-1} 
     * @return A double which represents the number saved as last result in memory.
     */
    public static double getLastResult(double[] memory, int memoryPointer) {
        int lastIndex = memoryPointer;
        if (lastIndex - 1 < 0) {
            lastIndex = memory.length - 1;
        } else {
            lastIndex--;
        }
        return memory[lastIndex];
    }


    /**
     * Reads the first number or instruction typed by user, to decide the next step in the
     * calculation {view printInfo method}. Evaluating if the line typed refers to a number, non
     * base-10 number or a memory access, then, do the correct action and call doNumberOperation
     * method to continue with operation.
     * 
     * @param sc     Input channel of application
     * @param memory Memory:Array:double of application, used for accessing of memory-saved numbers
     * @return A String which contains the result of the operation or an error message if the
     *         operation could not be realized.
     */
    public static String readAndOperate(Scanner sc, double[] memory) {
        String line = sc.nextLine();
        String result = "Error";
        if (line != null && !line.isEmpty()
                && (isANumber(line) || isMemoryAccess(line) || isNonDecimalNumber(line))) {
            if (isMemoryAccess(line)) {
                line = getMemoryAccess(line, memory);
            } else if (isNonDecimalNumber(line)) {
                line = getNonDecimalNumber(line) + "";
            }
            result = doNumberOperation(sc, line, memory);
        }
        return result;
    }

    /**
     * This method evalueate if the line is a number, verifying if the line is empty or if the first
     * character if - or each character is a digit.
     * 
     * @param line Represents the line which will be evaluated. Line must not be null
     * @return As a boolean value which turns on True if the line represents a number of false
     *         instead
     */

    public static boolean isANumber(String line) {
        boolean isANumber = true;

        if (line.isEmpty()) {
            isANumber = false;
        } else {
            int index = 0;
            char c = line.charAt(index);

            if (c == '-' || c == '+') {
                index++;
            }

            for (; index < line.length() && isANumber; index++) {
                c = line.charAt(index);
                if (!Character.isDigit(c)) {
                    isANumber = false;
                }
            }
        }

        return isANumber;
    }

    /**
     * Returns a line as the double number which represents, for example, if line = 'A', this method
     * returns 10, or if line = 'Pi' will return 3,141516....
     * <p>
     * <b>pre:</b>
     * </p>
     * Line must was validated by {@link #isNonDecimalNumber(String)} method before this method call.
     * 
     * @param line represents an hexadecimal value or Pi. line != null, {@code line.length >=0}, line is not
     *             empty.
     * @return A real number, which value of line or -1 if line is an incorrect value.
     */
    public static double getNonDecimalNumber(String line) {
        double number = -1;
        if (line.toLowerCase().equals("pi")) {
            number = Math.PI;
        } else {
            number = tenBaseConversion(line, 16);
        }
        return number;
    }

    /**
     * Verify if a line is a simple hexadecimal value (e.g. ABC12, A1, D3, E3, 10, AF or 1-9) or if
     * is the Pi number
     * 
     * @param line the line that will be validated. line != null. line must not be empty.
     *             {@code line.lenght>0}
     * @return True if the lowered-case line is equals to either a, b, c, d, e, f or 'pi'. False at
     *         instead.
     */

    public static boolean isNonDecimalNumber(String line) {
        String lowerLine = line.toLowerCase();
        boolean isNonDecimalNumber = lowerLine.equals("pi");
        int length = lowerLine.length();
        int totalOfLetters = 0;
        for (int i = 0; i < length && !isNonDecimalNumber; i++) {
            char c = lowerLine.charAt(i);
            if (c == 'a' || c == 'b' || c == 'c' || c == 'd' || c == 'e' || c == 'f') {
                totalOfLetters++;
            } else if (Character.isDigit(c)) {
                isNonDecimalNumber = true;
            } else {
                isNonDecimalNumber = false;
            }

        }
        if (totalOfLetters == 0) {
            isNonDecimalNumber = false;
        }

        return isNonDecimalNumber;
    }

    /**
     * Get a value saved in memory using the line passed as parameter.
     * <p>
     * Example
     * </p>
     * For example if the line is equals to mem5 represents the 5th result saved in memory.
     * <p>
     * <b>Pre:</b>
     * </p>
     * Memory has been initialized and contains not null values.
     * 
     * @param line   represents the access to memory. line must not be null and must not be empty.
     *               also, line must be use the patter presented in calculator's info and must be
     *               represents and index between 0 and memory.length
     * @param memory represents the calculator's memory. It must not be null.
     * @return As string the value saved in the memory.
     */
    public static String getMemoryAccess(String line, double[] memory) {
        String access = line.trim().split("mem")[0];
        String number = "0";

        System.out.println("Accessing to " + access + " result");

        if (access.isEmpty() || access.length() > 1) {
            System.out.println("But doesn't exist this memory space, so number 0 will be used.");
        } else {
            char c = access.charAt(0);
            if (!Character.isDigit(c)) {
                System.out.println("But the access wasn't correct. (invalid number)");
            } else {
                int index = Integer.parseInt(c + "");
                if (index < 0 || index >= memory.length) {
                    System.out.println("But the access wasn't correct. (invalid index)");
                } else {
                    number = memory[index] + "";
                }
            }
        }

        return number;
    }

    /**
     * Verify if the line contains the 'mem' command, used for memory access.
     * 
     * @param line represents the command to access to memory. {@code line != null} and line must not be
     *             empty- The line must be use the mem access pattern presented at calculator's info
     * @return A boolean value which is True if the line contains the 'mem' command or False
     *         instead.
     */
    public static boolean isMemoryAccess(String line) {

        return line.trim().contains("mem");
    }

    /**
     * Read the next input from user, which represents the operation to do. Then, use the
     * {@link #inOperation(double, String, Scanner, double[])} Method to do the operation and return its result.
     * 
     * <p><b>Pre:</b></p> Memory has already been initialized.
     * 
     * @param sc   represents the scanner of the program. It is the input stream. sc must not be
     *             null and its stream must be open.
     * @param line represents the last line read
     * @param memory Represents the memory of the calculator. Memory must not be null and {@code memory.length == 10}
     * @return An String which contains the result of operation
     */
    public static String doNumberOperation(Scanner sc, String line, double[] memory) {
        String result = "Error in doNumberOperation";
        double number = Double.parseDouble(line);
        String operation = sc.nextLine();
        if (operation != null && !operation.isEmpty()) {
            result = inOperation(number, operation, sc, memory);
        }

        return result;
    }

    /**
     * This method evaluate if the operation passed as parameter is an one-number operation or not,
     * then if the operation is a one-number operation call the correct calculus function with the
     * parameters, but else, read the nextNumber and calls {@link #doOperate(double, String, double)} to execute a two-number
     * operation.
     * <p><b>Pre:</b></p> memory has already been initialized.
     * 
     * @param number    Represents the first number to operate. number must not be NaN
     * @param operation Represents the selected operation by the user. operation must not be null or
     *                  empty
     * @param sc        Represents the scanner (input stream of the program). sc's stream must not
     *                  be closed
     * @param memory Represents the memory of the app. memory must not be null and {@code memory.length ==10}
     * @return The result of the selected operation as String.
     */

    public static String inOperation(double number, String operation, Scanner sc,
            double[] memory) {
        String result = "Error in inOperation method";
        operation = operation.toLowerCase();
        if (operation.equals("!")) {
            result = factorial(number) + "";
        } else if (operation.equals("l")) {
            result = log(number) + "";
        } else if (operation.equals("dg")) {
            result = toDegree(number) + "";
        } else if (operation.equals("rd")) {
            result = toRadian(number) + "";
        } else if (operation.equals("r")) {
            result = sqrt(number) + "";
        } else if (operation.equals("cos")) {
            result = cos(number) + "";
        } else if (operation.equals("sin")) {
            result = sin(number) + "";
        } else if (operation.equals("tan")) {
            result = tan(number) + "";
        } else if (operation.equals("tbn")) {
            result = toScientificNotation(number);
        } else {
            String auxLine = sc.nextLine();
            if (auxLine != null && !auxLine.isEmpty() && (isANumber(auxLine)
                    || isMemoryAccess(auxLine) || isNonDecimalNumber(auxLine))) {
                if (isNonDecimalNumber(auxLine)) {
                    auxLine = getNonDecimalNumber(auxLine) + "";
                }
                if (isMemoryAccess(auxLine)) {
                    auxLine = getMemoryAccess(auxLine, memory);
                }

                double auxNumber = Double.parseDouble(auxLine);
                result = doOperate(number, operation, auxNumber);
            }
        }
        return result;
    }

    /**
     * This method allows to do a two-number operation. With selected operation this method do
     * operations as: sum, multiplication, subtraction, module, division. or do operations as:
     * baseExchange (calling {@link #baseExchange(double, double)}), nlog and nth-square (calling {@link #nLog(double, double)}
     * and {@link #nthSquare(double, double)}) 
     * 
     * @param number    Represents the first number to operate. number must be a double, number must
     *                  not be nan.
     * @param operation Represents a two-number operation from the calculators info. operation must
     *                  not be null or empty
     * @param number2   Represents the second numbert to operate. number must be a double, number
     *                  must not be nan.
     * @return An string which contain the result of the operation
     */

    public static String doOperate(double number, String operation, double number2) {
        String result = "Error in doOperate method";
        switch (operation.toLowerCase().trim()) {
            case "*": {
                result = number * number2 + "";
                break;
            }
            case "/": {
                if (number2 == 0) {
                    result = "Error in doOperate method, division by 0";
                } else {
                    result = number / number2 + "";
                }
                break;
            }
            case "%": {
                if (number2 == 0) {
                    result = "Error in doOperate method, module number was 0";
                } else {
                    result = number % number2 + "";
                }
                break;
            }
            case "+": {
                result = number + number2 + "";
                break;
            }
            case "-": {
                result = number - number2 + "";
                break;
            }
            case "^": {
                result = pow(number, number2) + "";
                break;
            }
            case "to": {
                result = baseExchange(number, number2);
                break;
            }
            case "ln": {
                result = nLog(number, number2) + "";
                break;
            }
            case "rn": {
                result = nthSquare(number, number2) + "";
                break;
            }
        }
        return result;
    }



    // TODO:
    // MATH OPERATIONS


    // 6 - sqrt
    /**
     * Find the sqrt of a number using the formula: sqrt = ( (sqrt/2)+(number/sqrt/2) ) / 2
     * 
     * @param number Represents the number. number must be a double and not be Nan.
     * @return A double value which represents the sqrt
     */
    public static double sqrt(double number) {
        double temp;

        double sr = number / 2;

        do {
            temp = sr;
            sr = (temp + (number / temp)) / 2;
        } while ((temp - sr) != 0);

        return sr;
    }

    // 7 - Pow
    /**
     * Do a pow operation, multiplying the number by it-self power times
     * <p>
     * Pre:
     * </p>
     * Params must be double values and must not be Nan
     * 
     * @param number Represents the number which will be pow
     * @param power  Represents the exponent of the number. {@code power must be >=0}
     * @return A double which contain the pow result
     */
    public static double pow(double number, double power) {
        double result = 1;
        if (power > 0) {
            result = number;
            for (int i = 1; i < power; i++) {
                result *= number;
            }
        }
        return result;
    }

    // 8 - Factorial
    /**
     * This method do factorial operation, using the number parameter. Returns 1 if {@code number ==1 ||
     * number == 0} or return 0 if {@code number < 0}.
     * 
     * @param number Represents the number. number must be a double value and mustn't be nan.
     * @return A double value which contains the factorial result.
     */
    public static double factorial(double number) {
        double result = 0;
        if (number == 1 || number == 0) {
            result = 1;
        } else if (number > 1) {
            result = number;
            for (double i = result - 1; i > 1; i--) {
                result *= i;
            }
        } else {
            result = 0;
        }
        return result;
    }

    // 9 - base exchange
    /**
     * This method do a base exchange operation using {@link #binaryConversion(double)} and {@link #hexadecimalConversion(double)} allowing to converts to binary or hexadecimal
     * number. 
     * 
     * @param number Which will be converted. mumber must be a double and must not be NaN. number
     *               must be in decimal base
     * @param base   Represents the base used for convertion. Must be a double value of 2 or 16.
     * @return The result as String.
     */
    public static String baseExchange(double number, double base) {

        // number is already base-10 number
        String result = number + "B-10";
        int auxBase = (int) base;
        switch (auxBase) {
            case 2: {
                result = binaryConversion(number) + "B-2";
                break;
            }
            case 16: {
                result = hexadecimalConversion(number) + "B-16";
                break;
            }
        }
        return result;
    }


    // 9.1.1 - binary conversion
    /**
     * This method converts a number in its binary representation using its residues by 2
     * 
     * @param number Represents base-10 number. Must be a double number and must not be NaN.
     * @return An String which contains the result.
     */
    public static String binaryConversion(double number) {

        String result = "";

        int current = (int) number;
        do {

            int mod = current % 2;
            result = mod + result;
            mod %= 2;
            current /= 2;
        } while (current >= 2);

        result = current + result;

        return result;
    }

    // 9.2 - base-10 conversion
    /**
     * Converts a base-{base} number to a base-10 number. Only works with binary or hexadecimal
     * numbers. This method works calculating the distance of each string's character from '0' or
     * 'A' (for hexadecimal numbers) and in last case aproximate adding 10 for the distance. Then
     * calculating with this value the pow with current exp.
     * 
     * @param number Represents the number which will be converted. number must be a double value
     *               and {@code number>=0}. This param must not be a NaN
     * @param base   Represents the current base of the number. Must be an integer value. Must be
     *               only 2 or 16
     * @return A double which contains the base-10 result.
     */
    public static double tenBaseConversion(String number, int base) {

        char[] numberArray = number.toUpperCase().toCharArray();
        double result = 0;
        int pow = 0;
        for (int i = numberArray.length - 1; i >= 0; i--) {
            int n = 0;
            char tmp = numberArray[i];
            if (tmp >= '0' && tmp <= '9') {
                n = tmp - '0';
            } else if (tmp >= 'A' && tmp <= 'F') {
                n = tmp - 'A';
                n += 10;
            }
            double pw = pow(base, pow);
            result += n * pw;
            pow++;
        }

        return result;
    }



    // 9.3 - hexadecimal conversion
    /**
     * This method converts a number in its binary representation using its residues by 16
     * 
     * @param number Represents base-10 number. Must be a double number and must not be NaN.
     * @return An String which contains the result.
     */
    public static String hexadecimalConversion(double number) {

        String result = "";
        int mod = (int) number % 16;
        int current = (int) number / 16;
        while (current >= 16) {
            result = mod + result;
            mod %= 16;
            current /= 16;
        }

        result = current + result;

        return result;
    }

    // TODO:
    // 11 - Scientis conversion
    /**
     * Converts to Scientific Notation a base-10 number, using the commaIndex as reference to get
     * the number's Scientific notation.
     * 
     * @param number Represents the number which will be converted. number must be a double value
     *               and must not be NaN.
     * @return An String which contains the scientific notation with patter: {number}x10to{exp}
     */
    public static String toScientificNotation(double number) {

        String strNumber = number + "";
        strNumber = strNumber.replaceAll(",", ".");
        int exp = 0;
        String result = "";

        if (strNumber.length() > 1) {
            int commaIndex = strNumber.indexOf(".");
            if (commaIndex == -1) {
                commaIndex = strNumber.length() - 1;
            }
            // 1233,6 - 1,2336
            exp = (commaIndex - 1);

            int lastIndex = 1;
            if (strNumber.charAt(0) == '-') {
                result = strNumber.charAt(0) + "" + strNumber.charAt(1) + ".";
                exp = (commaIndex - 2);
                lastIndex = 2;
            } else {
                result = strNumber.charAt(0) + ".";
            }

            for (int i = lastIndex; i < commaIndex; i++) {
                result += strNumber.charAt(i);
            }
            for (int i = commaIndex + 1; i < strNumber.length(); i++) {
                result += strNumber.charAt(i);
            }
            result += "x10to" + exp;

        } else {
            result = strNumber + "x 10to0";
        }


        return result;
    }

    // 12 - Radians conversions
    /**
     * Converts a number to radians (using {@link Math#toRadians(double)})
     * 
     * @param number Represents the number which will be converted, number must be a double value and must
     *                   not be NaN
     * @return A double which contains the number in radians.
     */
    public static double toRadian(double number) {
        return Math.toRadians(number);
    }

    // 12-2 Degree conversion
    /**
     * Converts a number to degree (using {@link Math#toDegrees(double)})
     * 
     * @param number Represents the number which will be converted, number must be a double value and must
     *                   not be NaN
     * @return A double which contains the number in degrees.
     */
    public static double toDegree(double number) {
        return Math.toDegrees(number);
    }

    // 13 - Sine

    /**
     * Calculate the sin of a number (in degrees) using {@link Math#sin(double)}
     * 
     * @param number Represents the number for which the sine is calculated, number must be a double value
     *                   and must not be NaN
     * @return A double which contains the sine of the number.
     */
    public static double sin(double number) {

        return Math.sin(Math.toRadians(number));
    }

    // 14 - Cosine
    /**
     * Calculate the sin of a number (in degrees) using {@link Math#cos(double)}
     * 
     * @param number Represents the number for which the cosine is calculated, number must be a double
     *                   value and must not be NaN
     * @return A double which contains the cosine of the number.
     */
    public static double cos(double number) {

        return Math.cos(Math.toRadians(number));
    }


    // 15 - Tangent

    /**
     * Calculates the tangent of a given number. Using {@link #sin(double)} and {@link #cos(double)}. Then do sin/cos operaton.
     * 
     * @param number Represents the number for which tanget will be calculated. number must be a
     *               double and must not be NaN.
     * @return A double which contains the number's tangent.
     */
    public static double tan(double number) {
        double sin = sin(number);
        double cos = cos(number);
        return sin / cos;
    }

    // 16 - Log
    /**
     * Calculates the base-10 logarithm of a given number. Using {@link Math#log10(double)}.
     * 
     * @param number Represents the number for which base-10 logarithm will be calculated. number
     *               must be a double and must not be NaN.
     * @return A double which contains the number's base-10 logarithm.
     */
    public static double log(double number) {

        return Math.log10(number);
    }



    // 16-2 - n-log
    /**
     * Calculates the base-n logarithm of a given number. Using {@link #log(double)} and the logarithm base
     * exchange property.
     * 
     * @param number Represents the number for which base-n logarithm will be calculated. number
     *               must be a double and must not be NaN.
     * @param n Represents the n of the logartihm. n must be a double and must not be NaN. {@code number >=0}
     * @return A double which contains the number's base-n logarithm.
     */
    public static double nLog(double number, double n) {
        double a = log(number);
        double b = log(n);
        return a / b;
    }



    // 17 - n-th root
    /**
     * Calculates the nthSquare of a number using {@link Math#pow(double, double)}, {@link Math#E} and {@link Math#log(double)} with the
     * property: euler^(log(n)/n);
     * 
     * @param base Represents the number for which nth-square will be calculated. base must be a
     *             double and must not be NaN.
     * @param n    Represents the n of the square, must be a double and must not be NaN.
     * @return A double value which cointains the n-th square of the given number.
     */
    public static double nthSquare(double base, double n) {
        double result = Math.pow(Math.E, Math.log(base) / n);
        return result;
    }



}
