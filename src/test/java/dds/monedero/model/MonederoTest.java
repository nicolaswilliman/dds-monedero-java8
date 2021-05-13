package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonederoTest {
  @Test
  void PonerMontoNegativo() {
    Cuenta cuenta = obtenerCuentaConSaldo(0);
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }

  @Test
  void TresDepositos() {
    Cuenta cuenta = obtenerCuentaConSaldo(0);
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
  }

  @Test
  void MasDeTresDepositos() {
    Cuenta cuenta = obtenerCuentaConSaldo(0);
    assertThrows(MaximaCantidadDepositosException.class, () -> {
      cuenta.poner(1500);
      cuenta.poner(456);
      cuenta.poner(1900);
      cuenta.poner(245);
    });
  }

  @Test
  void ExtraerMasQueElSaldo() {
    Cuenta cuenta = obtenerCuentaConSaldo(1000);
    assertThrows(SaldoMenorException.class, () -> {
      cuenta.sacar(1001);
    });
  }

  @Test
  public void ExtraerMasDe1000() {
    Cuenta cuenta = obtenerCuentaConSaldo(5000);
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.sacar(1001);
    });
  }

  @Test
  public void ExtraerMontoNegativo() {
    Cuenta cuenta = obtenerCuentaConSaldo(0);
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }

  private Cuenta obtenerCuentaConSaldo(double saldo) {
    Cuenta cuenta = new Cuenta();
    cuenta.poner(saldo);
    return cuenta;
  }
}