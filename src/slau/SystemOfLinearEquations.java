package slau;

public class SystemOfLinearEquations {
  static public Vector ResolveWithTridiagonalAlgorithm(Vector a, Vector b, Vector c, Vector d) throws Exception {
    return ResolveWithTridiagonalAlgorithm(new TriDiagonalMatrix(a, b, c), d);
  }

  static public Vector ResolveWithTridiagonalAlgorithm(TriDiagonalMatrix matrix, Vector d) throws Exception {
    if (d.getSize() != matrix.getSize()) {
      throw new Exception("Размер матрицы не соответствует размеру вектора");
    }
    int n = matrix.getSize();
    float[] L = new float[n + 1];
    float[] M = new float[n + 1];
    for (int i = 0; i < n; ++i) {
      float c = matrix.getC(i);
      float b = matrix.getB(i);
      float a = matrix.getA(i);
      L[i + 1] = c / (b - a * L[i]);
      M[i + 1] = (d.getComponent(i) - matrix.getA(i) * M[i]) / (matrix.getB(i) - matrix.getA(i) * L[i]);
    }
    float[] x = new float[n];
    x[n - 1] = M[n];
    for (int i = n - 2; i >= 0; --i) {
      x[i] = M[i + 1] - L[i + 1] * x[i + 1];
    }
    return new Vector(x);
  }

  static public Vector notStableMethod(Vector a, Vector b, Vector c, Vector d) throws Exception {
    return notStableMethod(new TriDiagonalMatrix(a, b, c), d);
  }

  static public Vector notStableMethod(TriDiagonalMatrix matrix, Vector d) throws Exception {
    for (int i = 0; i < matrix.getSize() - 1; ++i) {
      if (matrix.getC(i) == 0) {
        throw new Exception("Невозможно найти решние, вектор 'c' содержит нули");
      }
    }
    int n = matrix.getSize();
    float[] y = new float[n];
    float[] z = new float[n];
    y[0] = 0;
    z[0] = 1;
    y[1] = d.getComponent(0) / matrix.getC(0);
    z[1] = - matrix.getB(0) / matrix.getC(0);
    for (int i = 1; i < n - 1; ++i) {
      y[i + 1] = (d.getComponent(i) - matrix.getA(i) * y[i - 1] - matrix.getB(i) * y[i]) / matrix.getC(i);
      z[i + 1] = -(matrix.getA(i) * z[i - 1] + matrix.getB(i) * z[i]) / matrix.getC(i);
    }
    float K = (d.getComponent(n - 1) - matrix.getA(n - 1) * y[n - 2] - matrix.getB(n - 1) * y[n - 1]) /
            (matrix.getA(n - 1) * z[n - 2] + matrix.getB(n - 1) * z[n - 1]);
    float[] x = new float[n];
    for (int i = 0; i < n; ++i) {
      x[i] = y[i] + K * z[i];
    }
    return new Vector(x);
  }
}
