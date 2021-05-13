package dds.monedero.model;

import java.time.LocalDate;

public abstract class Movimiento {
  private LocalDate fecha;
  //En ningún lenguaje de programación usen jamás doubles para modelar dinero en el mundo real
  //siempre usen numeros de precision arbitraria, como BigDecimal en Java y similares
  private double monto;

  public Movimiento(LocalDate fecha, double monto) {
    this.fecha = fecha;
    this.monto = monto;
  }

  public double getMonto() {
    return monto;
  }

  public LocalDate getFecha() {
    return fecha;
  }

  public boolean fueDepositado(LocalDate fecha) {
    return elTipoDeMovimientoEs(TipoDeMovimiento.DEPOSITO) && esDeLaFecha(fecha);
  }

  public boolean fueExtraido(LocalDate fecha) {
    return elTipoDeMovimientoEs(TipoDeMovimiento.EXTRACCION) && esDeLaFecha(fecha);
  }

  public boolean esDeLaFecha(LocalDate fecha) {
    return this.fecha.equals(fecha);
  }

  public boolean isDeposito() {
    return elTipoDeMovimientoEs(TipoDeMovimiento.DEPOSITO);
  }

  public boolean isExtraccion() {
    return elTipoDeMovimientoEs(TipoDeMovimiento.EXTRACCION);
  }

  public abstract Boolean elTipoDeMovimientoEs(TipoDeMovimiento tipo);

  public double calcularValor(Cuenta cuenta) {
    if (this.elTipoDeMovimientoEs(TipoDeMovimiento.DEPOSITO)) {
      return cuenta.getSaldo() + getMonto();
    } else {
      return cuenta.getSaldo() - getMonto();
    }
  }
}
