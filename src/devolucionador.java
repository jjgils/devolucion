import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class devolucionador extends JFrame {
    private JTextField txtMontoPagar, txtMontoRecibido, txtCantidadesDisponibles[];
    private JTable tblDevolucion;
    private JButton btnRealizarCalculo, btnActualizarCaja;

    private int[] valoresDenominacion = { 20000, 10000, 5000, 2000, 1000, 500, 200, 100, 50 };
    private String[] tipoDeDenominacion = { "Mil Pesos COL o LKS", " Mil Pesos COL o LKS", "Mil Pesos COL o LKS", "Mil Pesos COL o LKS", "Mil Pesos COL o LKS",
            "Pesos COL", "Pesos COL", "Pesos COL", "Pesos COL" };

    public devolucionador() {
        setTitle("Calculadora de Devolución");
        setSize(500, 530);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblMontoPagar = new JLabel("valor total:");
        JLabel lblMontoRecibido = new JLabel("Dinero recibido:");
        JLabel lblCantidadesDisponibles = new JLabel("Cantidades de denominaciones:");

        txtMontoPagar = new JTextField();
        txtMontoRecibido = new JTextField();
        btnRealizarCalculo = new JButton("calcular");
        btnActualizarCaja = new JButton("reiniciar");

        lblMontoPagar.setBounds(10, 20, 60, 25);
        txtMontoPagar.setBounds(70, 20, 50, 25);
        lblMontoRecibido.setBounds(160, 20, 140, 25);
        txtMontoRecibido.setBounds(250, 20, 100, 25);
        btnRealizarCalculo.setBounds(10, 50, 90, 25);
        lblCantidadesDisponibles.setBounds(10, 80, 200, 25);
        btnActualizarCaja.setBounds(120, 50, 90, 25);

        add(lblMontoPagar);
        add(txtMontoPagar);
        add(lblMontoRecibido);
        add(txtMontoRecibido);
        add(btnRealizarCalculo);
        add(lblCantidadesDisponibles);
        add(btnActualizarCaja);

        txtCantidadesDisponibles = new JTextField[valoresDenominacion.length];
        for (int i = 0; i < valoresDenominacion.length; i++) {
            JLabel lblDenominacion = new JLabel("$ " + valoresDenominacion[i] + " (" + tipoDeDenominacion[i] + ")");
            lblDenominacion.setBounds(20, 110 + (i * 25), 180, 25);
            txtCantidadesDisponibles[i] = new JTextField("0");
            txtCantidadesDisponibles[i].setBounds(200, 110 + (i * 25), 50, 25);
            add(lblDenominacion);
            add(txtCantidadesDisponibles[i]);
        }

        String[] columnas = { "valores", "Tipo", "Cantidades" };
        tblDevolucion = new JTable(new DefaultTableModel(columnas, 0));
        JScrollPane scrollPane = new JScrollPane(tblDevolucion);
        scrollPane.setBounds(20, 110 + (valoresDenominacion.length * 25), 450, 150);
        add(scrollPane);

        btnRealizarCalculo.addActionListener(e -> calcularDevolucion());

        btnActualizarCaja.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarCaja();
            }
        });
    }

    private void calcularDevolucion() {
        try {
            int montoPagar = Integer.parseInt(txtMontoPagar.getText());
            int montoRecibido = Integer.parseInt(txtMontoRecibido.getText());

            if (montoRecibido < montoPagar) {
                JOptionPane.showMessageDialog(this, "el valor recibido tiene que ser mayor al valor al pagar", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int cambio = montoRecibido - montoPagar;
            int[] cantidadesDisponibles = new int[valoresDenominacion.length];

            for (int i = 0; i < valoresDenominacion.length; i++) {
                try {
                    cantidadesDisponibles[i] = Integer.parseInt(txtCantidadesDisponibles[i].getText());
                    if (cantidadesDisponibles[i] < 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Ingresa las cantidades validas", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            DefaultTableModel modelo = (DefaultTableModel) tblDevolucion.getModel();
            modelo.setRowCount(0);

            for (int i = 0; i < valoresDenominacion.length; i++) {
                int cantidad = Math.min(cambio / valoresDenominacion[i], cantidadesDisponibles[i]);
                if (cantidad > 0) {
                    modelo.addRow(new Object[] { "$ " + valoresDenominacion[i], tipoDeDenominacion[i], cantidad });
                    cambio -= cantidad * valoresDenominacion[i];
                }
            }

            if (cambio > 0) {
                JOptionPane.showMessageDialog(this, "Con el stock actual no es posible devolver esta cantidad, Es posible que no se puso cuanto dinero tiene la caja registradora",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ingresa solo valores validos, esto es para calcular no para escribir", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarCaja() {
        for (int i = 0; i < txtCantidadesDisponibles.length; i++) {
            txtCantidadesDisponibles[i].setText("0");
        }
        DefaultTableModel modelo = (DefaultTableModel) tblDevolucion.getModel();
        modelo.setRowCount(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            devolucionador ventana = new devolucionador();
            ventana.setVisible(true);
        });
    }
}
