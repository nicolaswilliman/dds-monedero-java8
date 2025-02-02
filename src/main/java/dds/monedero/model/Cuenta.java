package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private List<Movimiento> movimientos = new ArrayList<>();

  public void poner(double cuanto) {
    validarMonto(cuanto);

    if (getMovimientos().stream().filter(movimiento -> movimiento.elTipoDeMovimientoEs(TipoDeMovimiento.DEPOSITO)).count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }

    movimientos.add(new Deposito(LocalDate.now(), cuanto));
  }

  public void sacar(double cuanto) {
    validarMonto(cuanto);

    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
    movimientos.add(new Extraccion(LocalDate.now(), cuanto));
  }


  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> movimiento.elTipoDeMovimientoEs(TipoDeMovimiento.EXTRACCION) && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    Double depositoTotal = montoTotalSegunTipo(TipoDeMovimiento.DEPOSITO);
    Double extraccionTotal = montoTotalSegunTipo(TipoDeMovimiento.EXTRACCION);
    return depositoTotal - extraccionTotal;
  }

  private double montoTotalSegunTipo(TipoDeMovimiento tipo) {
    return getMovimientos().stream()
    .filter(movimiento -> movimiento.elTipoDeMovimientoEs(tipo))
    .mapToDouble(Movimiento::getMonto)
    .sum();
  }

  private void validarMonto(double monto) {
    if (monto <= 0) throw new MontoNegativoException(monto + ": el monto a ingresar debe ser un valor positivo");
  }
}
