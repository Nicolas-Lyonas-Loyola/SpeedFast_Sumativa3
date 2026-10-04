package cl.duoc.speedfast.dao;

import cl.duoc.speedfast.model.Pedido;

import java.util.List;

public interface PedidoDAO {

    boolean create(Pedido pedido);

    List<Pedido> readAll();

    boolean update(Pedido pedido);

    boolean delete(int id);
}