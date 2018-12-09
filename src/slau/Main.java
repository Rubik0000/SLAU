package slau;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  static public void main(String[] args) {
    try {
      mainMenu();
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static void mainMenu() throws Exception {
    try (Scanner scanner = new Scanner(System.in)) {
      for (; ; ) {
        int ans = 0;
        do {
          System.out.println("Веберите режим работы:");
          System.out.println("1 - основной режим");
          System.out.println("2 - режим тестрирования");
          System.out.println("0 - отмена");
          if (!scanner.hasNextInt()) {
            continue;
          }
          ans = scanner.nextInt();
        } while (ans < 0 || ans > 2);
        switch (ans) {
          case 0:
            return;

          case 1:
            usualMode(scanner);
            break;

          case 2:
            testMode(scanner);
            break;
        }
      }
    }
  }

  static private void usualMode(Scanner scanner) throws Exception {
    System.out.println("Введите размер системы");
    int size = scanner.nextInt();

    System.out.println("Введите вектор a:");
    Vector a = getVector(size, scanner);
    if (a == null) {
      return;
    }
    if (a.getSize() != size) {
      throw new Exception("Неподходящий размер вектора");
    }

    System.out.println("Введите вектор b:");
    Vector b = getVector(size, scanner);
    if (b == null) {
      return;
    }
    if (b.getSize() != size) {
      throw new Exception("Неподходящий размер вектора");
    }

    System.out.println("Введите вектор c:");
    Vector c = getVector(size, scanner);
    if (c == null) {
      return;
    }
    if (c.getSize() != size) {
      throw new Exception("Неподходящий размер вектора");
    }

    System.out.println("Введите вектор d:");
    Vector d = getVector(size, scanner);
    if (d == null) {
      return;
    }
    if (d.getSize() != size) {
      throw new Exception("Неподходящий размер вектора");
    }
    int ans = 0;
    do {
      System.out.println("1 - метод прогонки");
      System.out.println("2 - неустойчивый метод");
      System.out.println("0 - отмена");
      ans = scanner.nextInt();
    } while (ans < 0 || ans > 2);
    Vector result;
    switch (ans) {
      case 0:
        return;

      case 1:
        result = SystemOfLinearEquations.ResolveWithTridiagonalAlgorithm(a, b, c, d);
        break;

      case 2:
        result = SystemOfLinearEquations.notStableMethod(a, b, c, d);
        break;

      default:
        return;
    }
    do {
      System.out.println("1 - вывести на консоль");
      System.out.println("2 - записать в файл");
      System.out.println("0 - отмена");
      ans = scanner.nextInt();
    } while (ans < 0 || ans > 2);
    switch (ans) {
      case 0:
        return;

      case 1:
        result.writeToConsole();
        break;

      case 2:
        System.out.println("Введите имя файла");
        String fileName = scanner.next();
        result.writeToFile(fileName);

      default:
        return;
    }
  }


  static private void testMode(Scanner scanner) throws Exception {
    ArrayList<Integer> systemSize = new ArrayList<>();
    ArrayList<Float> triDigMethError = new ArrayList<>();
    ArrayList<Float> nonStableError = new ArrayList<>();
    for (; ; ) {
      System.out.println("Введите размер системы");
      int size = scanner.nextInt();

      System.out.println("Введите вектор a:");
      Vector a = getVector(size, scanner);
      if (a == null) {
        return;
      }
      if (a.getSize() != size) {
        throw new Exception("Неподходящий размер вектора");
      }

      System.out.println("Введите вектор b:");
      Vector b = getVector(size, scanner);
      if (b == null) {
        return;
      }
      if (b.getSize() != size) {
        throw new Exception("Неподходящий размер вектора");
      }

      System.out.println("Введите вектор c:");
      Vector c = getVector(size, scanner);
      if (c == null) {
        return;
      }
      if (c.getSize() != size) {
        throw new Exception("Неподходящий размер вектора");
      }

      System.out.println("Введите вектор точного решения x*:");
      Vector xs = getVector(size, scanner);
      if (xs == null) {
        return;
      }
      if (xs.getSize() != size) {
        throw new Exception("Неподходящий размер вектора");
      }
      Vector d = new TriDiagonalMatrix(a, b, c).multiply(xs);
      Vector triDigMeth = SystemOfLinearEquations.ResolveWithTridiagonalAlgorithm(a, b, c, d);
      System.out.println("Метод прогонки:");
      triDigMeth.writeToConsole();
      Vector nonStbl = SystemOfLinearEquations.notStableMethod(a, b, c, d);
      System.out.println("Неустойчивый метод:");
      nonStbl.writeToConsole();

      systemSize.add(a.getSize());
      triDigMethError.add(getError(triDigMeth, xs));
      nonStableError.add(getError(nonStbl, xs));

      char ans;
      do {
        System.out.println("Продолжить тестирование? Y/N");
        ans = (char) System.in.read();
      } while (ans != 'n' && ans != 'N' && ans != 'y' && ans != 'Y');
      if (ans == 'n' || ans == 'N') {
        break;
      }
    }
    printTable(systemSize, triDigMethError, nonStableError);
  }

  static private void printLine(int len) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < len; ++i) {
      stringBuilder.append('-');
    }
    System.out.println(stringBuilder.toString());
  }

  static private void printTable(List<Integer> sysSize, List<Float> triGidMeth, List<Float> nonStable) {
    System.out.printf("|%-12s|%-12s|%-12s|", "Размер", "Прогонка", "Неуст.");
    System.out.println();
    printLine(40);
    for (int i = 0; i < sysSize.size(); ++i) {
      System.out.printf("|%12d|%12f|%12f|", sysSize.get(i), triGidMeth.get(i), nonStable.get(i));
      System.out.println();
    }
  }

  static private float getError(Vector vec1, Vector vec2) throws Exception {
    return vec1.subtract(vec2).getNorm();
  }

  static private Vector getVector(int size, Scanner scanner) throws Exception {
    int ans = 0;
    do {
      System.out.println("1 - считать из файла:");
      System.out.println("2 - ввести с консоли");
      System.out.println("3 - заполнить случайными числами");
      System.out.println("0 - отмена");
      ans = scanner.nextInt();
    } while (ans < 0 || ans > 3);
    Vector vec;
    switch (ans) {
      case 0:
        return null;

      case 1:
        System.out.println("Введите имя файла");
        String fileName = scanner.next();
        vec = new Vector(fileName);
        return vec;

      case 2:
        vec = new Vector();
        vec.readFromConsole(size);
        return vec;

      case 3:
        return Vector.getRandomVector(size);

      default:
        return null;

    }
  }
}
