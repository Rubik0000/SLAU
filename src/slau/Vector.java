package slau;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Random;
import java.util.Scanner;

public class Vector implements IOdata {
  static private Random random = new Random();

  private float[] _vector;

  public float getComponent(int ind) {
    return _vector[ind];
  }

  public int getSize() {
    return _vector.length;
  }

  public Vector(float ...values) {
    _vector = values;
  }

  public Vector(String filename) throws Exception {
    readFromFile(filename);
  }

  static public Vector getRandomVector(int size) {
    float[] vec = new float[size];
    float min = -100;
    float max = 100;
    for (int i = 0; i < size; ++i) {
      vec[i] = min + random.nextFloat() * (max - min);
    }
    return new Vector(vec);
  }

  public Vector add(Vector vec) throws Exception {
    if (getSize() != vec.getSize()) {
      throw new Exception("Невозможно сложить вектора разных размеров");
    }
    float[] newVec = new float[getSize()];
    for (int i = 0; i < getSize(); ++i) {
      newVec[i] = vec._vector[i] + _vector[i];
    }
    return new Vector(newVec);
  }

  public Vector subtract(Vector vec) throws Exception {
    if (getSize() != vec.getSize()) {
      throw new Exception("Невозможно вычесть вектора разных размеров");
    }
    float[] newVec = new float[getSize()];
    for (int i = 0; i < getSize(); ++i) {
      newVec[i] = _vector[i] - vec._vector[i];
    }
    return new Vector(newVec);
  }

  public float scalarMultiply(Vector vec) throws Exception {
    if (getSize() != vec.getSize()) {
      throw new Exception("Невозможно вычислить скалярное произведение векторов разных размеров");
    }
    float result = 0;
    for (int i = 0; i < getSize(); ++i) {
      result += _vector[i] * vec._vector[i];
    }
    return result;
  }

  public float getNorm() {
    float max = Math.abs(_vector[0]);
    for (int i = 1; i < getSize(); ++i) {
      if (Math.abs(_vector[i]) > max) {
        max = Math.abs(_vector[i]);
      }
    }
    return max;
  }

  @Override
  public void writeToConsole(Object ...args) {
    for (float el : _vector) {
      System.out.print(el + "  ");
    }
    System.out.println();
  }

  @Override
  public void readFromConsole(Object ...args) {
      Scanner scanner = new Scanner(System.in);
      int size = (Integer) args[0];
      System.out.println("Введите " + size + " компонент вектора: ");
      _vector = new float[size];
      for (int i = 0; i < size; ++i) {
        _vector[i] = scanner.nextFloat();
      }
  }

  @Override
  public void writeToFile(String filename) throws Exception {
    try (FileWriter writer = new FileWriter(filename)) {
      writer.write(getSize() + System.lineSeparator());
      for (float component : _vector) {
        writer.write(component + " ");
      }
      writer.write(System.lineSeparator());
    }
  }

  @Override
  public void readFromFile(String filename) throws Exception {
    try (Scanner scanner = new Scanner(new FileReader(filename))) {
      int size = scanner.nextInt();
      float[] newVec = new float[size];
      for (int i = 0; i < size; ++i) {
        newVec[i] = scanner.nextFloat();
      }
      _vector = newVec;
    }
  }
}
