package Reservations;

import Clients.Client;
import Clients.ClientManager;
import Contracts.Contract;
import Contracts.ContractList;
import Contracts.TariffType;
import Exceptions.*;
import GuiList.Clearable;
import GuiList.Requireable;
import GuiList.Saveable;
import GuiList.Searchable;
import GuiList.Showable;
import Utils.UtilDate;
import Utils.UtilGui;
import Vehicles.Vehicle;
import Vehicles.VehicleManager;
import Vehicles.VehicleType;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JFrame;

/**
 *
 * @author Brwni
 */
public class FrmReservation extends javax.swing.JFrame implements Requireable, Saveable, Searchable, Clearable, Showable {
    private VehicleManager managerV;
    private ClientManager managerCl;
    private ReservationList managerR;
    private ContractList managerCr;
    private Reservation reservacion;
    private List rList;
    private Contract contrato;
    private Client cliente;
    private Vehicle vehicle;
    private TariffType tariff;
    private DiaSearchReservation frmSearch;
    
    /**
     * Creates new form FrmReservations
     */
    public FrmReservation() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        initManagers();
        initFrames();
    }
    
    private void initFrames() {
        this.frmSearch = new DiaSearchReservation(this, true);
    }
    
    private void initManagers() {
        managerV = new VehicleManager();
        managerCl = new ClientManager();
        managerR  = ReservationList.getInstance();
    }
    
     @Override
    public boolean validateRequiere() {
        return UtilGui.validateRequiere(txtPlate, txtClient, txtFinalize);
    }

    @Override
    public void save() {
        if (validateRequiere()) {
            try {
                String id = txtClient.getText();
                String plate = txtPlate.getText();
                LocalDate initDate = UtilDate.toLocalDate(txtInitialize.getText());
                LocalDate finishDate = UtilDate.toLocalDate(txtInitialize.getText());
                tariff = getTariffType();
                
                cliente = managerCl.findClient(id);
                vehicle = managerV.findVehicle(plate);
                
                if (initDate == null) managerR.createReservation(cliente, vehicle, null, finishDate);
                else managerR.createReservation(cliente, vehicle, initDate, finishDate);
                
                contrato = new Contract(reservacion, tariff);
            
                managerCr.addContract(contrato);
                
            } catch (NoClientException | NoCarSelectedException | InvalidDateException | OverlappingReservationException | ContractAlreadyExistsException e) {
                UtilGui.showErrorMessage(this, e.getMessage(), "Error");
            }
            
            

            UtilGui.showMessage(this, "La reservacion y el contrato numero: " + contrato.getContractNum(), " han sido creados");
            clear();
        }else{
            UtilGui.showErrorMessage(this, "Faltan datos requeridos", "Error");
        }
    }

    @Override
    public void search() {
        String plate = txtPlate.getText();
        if (!plate.isBlank()) {
            try {
                this.vehicle = managerV.findVehicle(plate);

                if (this.vehicle != null) {
                    showData();
                } else {
                    this.vehicle = null;
                    clearCarFields();
                    UtilGui.showErrorMessage(this, "Vehiculo con placa " + plate + " no encontrado.", "Error de Busqueda");
                }
            } catch (Exception e) {
                UtilGui.showErrorMessage(this, "Ocurrió un error al buscar el vehículo.", "Error");
            }
        } else {
            UtilGui.showErrorMessage(this, "Por favor ingrese la placa del vehiculo para buscarlo.", "Error");
        }
    }
    
    public void searchReservation() {
        if (!txtClient.getText().isBlank()) {
            rList = managerR.searchByCliente(txtClient.getText());
        }
        else{
            UtilGui.showErrorMessage(this, "Faltan datos requeridos", "Error");
        }
    }

    @Override
    public void clear() {
        txtPlate.setText("");
        txtBrand.setText("");
        txtModel.setText("");
        txtType.setText("");
        txtYear.setText("");
        txtClient.setText("");
        txtInitialize.setText("");
        txtFinalize.setText("");
        txtTariff.setText("");
    }
    
    private void clearCarFields() {
        txtPlate.setText("");
        txtBrand.setText("");
        txtModel.setText("");
        txtType.setText("");
        txtYear.setText("");
    }

    @Override
    public void showData() {
        if (this.vehicle != null) {
            txtPlate.setText(this.vehicle.getPlate());
            txtBrand.setText(this.vehicle.getBrand());
            txtModel.setText(this.vehicle.getModel());
            txtType.setText(this.vehicle.getType().toString());
            txtYear.setText(String.valueOf(this.vehicle.getYear()));
        } else {
            clearCarFields();
        }
    }
    
    public VehicleType getTypeTxt(String txt) {
        if (VehicleType.SEDAN.equals(txtType.getText())) return VehicleType.SEDAN;
        else if (VehicleType.SUV.equals(txtType.getText())) return VehicleType.SUV;
        else if (VehicleType.PICKUP.equals(txtType.getText())) return VehicleType.PICKUP;
        else if (VehicleType.VAN.equals(txtType.getText())) return VehicleType.VAN;
        else if (VehicleType.MINIVAN.equals(txtType.getText())) return VehicleType.MINIVAN;
        else return VehicleType.MINIBUS;
    }
    
    private TariffType getTariffType() {
        if (VehicleType.SEDAN.equals(txtType.getText())) return TariffType.SEDAN;
        else if (VehicleType.SUV.equals(txtType.getText())) return TariffType.SUV;
        else if (VehicleType.PICKUP.equals(txtType.getText())) return TariffType.PICKUP;
        else if (VehicleType.VAN.equals(txtType.getText())) return TariffType.VAN;
        else if (VehicleType.MINIVAN.equals(txtType.getText())) return TariffType.MINIVAN;
        else return TariffType.MINIBUS;
    }
    
    public void modifyReservation() {
        if (this.reservacion == null) {
            UtilGui.showErrorMessage(this, "Por favor, busque y seleccione una reserva para modificar.", "Error de Modificación");
            return;
        }

        if (!UtilGui.validateRequiere(txtPlate, txtInitialize, txtFinalize)) {
            UtilGui.showErrorMessage(this, "Por favor, complete los campos de Placa, Fecha Inicio y Fecha Finalización para modificar.", "Datos Requeridos");
            return;
        }

        try {
            String oldVehiclePlate = this.reservacion.getCar().getPlate(); // Get the current plate
            String newVehiclePlate = txtPlate.getText(); // Get the new plate from the GUI

            LocalDate newStartDate = UtilDate.toLocalDate(txtInitialize.getText());
            LocalDate newFinishDate = UtilDate.toLocalDate(txtFinalize.getText());

            this.reservacion.modifyDates(newStartDate, newFinishDate);

            if (!oldVehiclePlate.equals(newVehiclePlate)) {
                managerR.modifyReservationVehicle(oldVehiclePlate, newVehiclePlate, this.reservacion);
            } else {

                Vehicle updatedVehicle = managerV.findVehicle(newVehiclePlate);
                this.reservacion.modifyCar(updatedVehicle);
            }

            UtilGui.showMessage(this, "Reserva modificada exitosamente.", "Modificación Exitosa");
            clear();

        } catch (NoCarSelectedException | OverlappingReservationException | InvalidDateException e) {
            UtilGui.showErrorMessage(this, e.getMessage(), "Error");
        }
    }
    
    private void populateWithReservation(Reservation res) {
        this.reservacion = res;

        txtClient.setText(res.getClient().getId());
        txtPlate.setText(res.getCar().getPlate());
        txtInitialize.setText(UtilDate.toString(res.getStartTime()));
        txtFinalize.setText(UtilDate.toString(res.getFinishTime()));

        txtBrand.setText(res.getCar().getBrand());
        txtModel.setText(res.getCar().getModel());
        txtType.setText(res.getCar().getType().toString());
        txtYear.setText(String.valueOf(res.getCar().getYear()));
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSearchR = new javax.swing.JButton();
        btnConfirm = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtYear = new javax.swing.JTextField();
        txtClient = new javax.swing.JTextField();
        txtTariff = new javax.swing.JTextField();
        txtType = new javax.swing.JTextField();
        txtBrand = new javax.swing.JTextField();
        txtModel = new javax.swing.JTextField();
        btnSearchV = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        txtFinalize = new javax.swing.JFormattedTextField();
        txtInitialize = new javax.swing.JFormattedTextField();
        txtPlate = new javax.swing.JTextField();
        btnModify = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1920, 1080));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnSearchR.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnSearchR.setText("Buscar Reserva");
        btnSearchR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchRActionPerformed(evt);
            }
        });
        getContentPane().add(btnSearchR, new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 840, 325, 133));

        btnConfirm.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnConfirm.setText("Confirmar Reserva");
        btnConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmActionPerformed(evt);
            }
        });
        getContentPane().add(btnConfirm, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 840, 325, 133));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Detalles de la reserva:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 210, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Cedula:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 260, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Placa:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 260, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Inicia:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 310, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Finaliza:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 360, -1, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Tipo:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 300, -1, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Fabricante:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 340, -1, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Modelo:");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 380, -1, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Año:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 420, -1, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Tarifa diaria:");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 420, -1, -1));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Apariencia fisica del vehiculo");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 460, -1, -1));

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/ToyotaCorolla.jpg"))); // NOI18N
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 510, -1, -1));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/GrupoDypurConjunto.png"))); // NOI18N
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 980));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/BannerPartners.png"))); // NOI18N
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(1500, 0, 384, 980));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/GrupoDypurSlogan.png"))); // NOI18N
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 1450, 210));

        txtYear.setEditable(false);
        txtYear.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtYear.setEnabled(false);
        getContentPane().add(txtYear, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 420, 300, -1));

        txtClient.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        getContentPane().add(txtClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 260, 230, -1));

        txtTariff.setEditable(false);
        txtTariff.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtTariff.setEnabled(false);
        getContentPane().add(txtTariff, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 420, 190, -1));

        txtType.setEditable(false);
        txtType.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtType.setEnabled(false);
        getContentPane().add(txtType, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 300, 290, -1));

        txtBrand.setEditable(false);
        txtBrand.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtBrand.setEnabled(false);
        getContentPane().add(txtBrand, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 340, 230, -1));

        txtModel.setEditable(false);
        txtModel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtModel.setEnabled(false);
        getContentPane().add(txtModel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 380, 250, -1));

        btnSearchV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/searchIcon.png"))); // NOI18N
        btnSearchV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchVActionPerformed(evt);
            }
        });
        getContentPane().add(btnSearchV, new org.netbeans.lib.awtextra.AbsoluteConstraints(1310, 260, 160, 90));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel16.setText("Buscar vehiculo");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(1330, 230, -1, -1));

        txtFinalize.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy"))));
        txtFinalize.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        getContentPane().add(txtFinalize, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 360, 230, -1));

        txtInitialize.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy"))));
        txtInitialize.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        getContentPane().add(txtInitialize, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 310, 250, -1));

        txtPlate.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        getContentPane().add(txtPlate, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 260, 290, -1));

        btnModify.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnModify.setText("Modificar Reserva");
        btnModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModifyActionPerformed(evt);
            }
        });
        getContentPane().add(btnModify, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 840, 325, 133));

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/clearIcon.png"))); // NOI18N
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        getContentPane().add(btnClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 260, 160, 90));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel17.setText("Limpiar datos");
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 230, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchVActionPerformed
        search();
    }//GEN-LAST:event_btnSearchVActionPerformed

    private void btnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmActionPerformed
        save();
    }//GEN-LAST:event_btnConfirmActionPerformed

    private void btnSearchRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchRActionPerformed
        searchReservation();
        String id = txtClient.getText();
        if (rList != null) {
            frmSearch.setIdFromFrm(id);
            frmSearch.setVisible(true);
            Reservation selectedRes = frmSearch.getSelectedReservation();
    
            if (selectedRes != null) {
                populateWithReservation(selectedRes);
            }
        }
    }//GEN-LAST:event_btnSearchRActionPerformed

    private void btnModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModifyActionPerformed
        modifyReservation();
    }//GEN-LAST:event_btnModifyActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clear();
    }//GEN-LAST:event_btnClearActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmReservation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmReservation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmReservation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmReservation.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmReservation().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnConfirm;
    private javax.swing.JButton btnModify;
    private javax.swing.JButton btnSearchR;
    private javax.swing.JButton btnSearchV;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField txtBrand;
    private javax.swing.JTextField txtClient;
    private javax.swing.JFormattedTextField txtFinalize;
    private javax.swing.JFormattedTextField txtInitialize;
    private javax.swing.JTextField txtModel;
    private javax.swing.JTextField txtPlate;
    private javax.swing.JTextField txtTariff;
    private javax.swing.JTextField txtType;
    private javax.swing.JTextField txtYear;
    // End of variables declaration//GEN-END:variables

}
