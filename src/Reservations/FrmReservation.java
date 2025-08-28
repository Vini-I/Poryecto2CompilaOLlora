package Reservations;

import Clients.Client;
import Clients.ClientManager;
import Contracts.Contract;
import Contracts.ContractList;
import Contracts.FrmContract;
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
    private FrmContract frmContract;
    
    /**
     * Creates new form FrmReservations
     */
    public FrmReservation(FrmContract parent) {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        initManagers();
        initFrames(parent);
    }
    
    private void initFrames(FrmContract parent) {
        this.frmSearch = new DiaSearchReservation(this, true);
        this.frmContract = parent;
    }
    
    private void initManagers() {
        managerV = new VehicleManager();
        managerCl = new ClientManager();
        managerCr = ContractList.getInstance();
        managerR  = ReservationList.getInstance();
    }
    
     @Override
    public boolean validateRequiere() {
        return UtilGui.validateRequiere(txtPlate, txtClient, txtInitialize, txtFinalize);
    }

    @Override
    public void save() {
        if (validateRequiere()) {
            try {
                String id = txtClient.getText();
                String plate = txtPlate.getText();
                LocalDate initDate = UtilDate.toLocalDate((txtInitialize.getText()));
                LocalDate finishDate = UtilDate.toLocalDate(txtFinalize.getText());
                tariff = getTariffType();
                
                cliente = managerCl.findClient(id);
                
                if (cliente == null) {
                    UtilGui.showErrorMessage(this, "No se encontró un cliente con la cédula proporcionada.", "Cliente No Encontrado");
                    return;
                }
                
                vehicle = managerV.findVehicle(plate);
                
                if (initDate == null) reservacion = managerR.createReservation(cliente, vehicle, null, finishDate);
                else reservacion = managerR.createReservation(cliente, vehicle, initDate, finishDate);
                
                contrato = new Contract(managerCr.getNextContractNumber(), reservacion, tariff);
            
                managerCr.addContract(contrato);
                
                this.frmContract.refreshTable();
                
                UtilGui.showMessage(this, "La reservacion y el contrato numero: " + contrato.getContractNum(), " han sido creados");
                clear();
                
            } catch (NoClientException | NoCarSelectedException | InvalidDateException | OverlappingReservationException | ContractAlreadyExistsException e) {
                UtilGui.showErrorMessage(this, e.getMessage(), "Error");
            }
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
        txtTariff.setText("");
    }

    @Override
    public void showData() {
        if (this.vehicle != null) {
            txtPlate.setText(this.vehicle.getPlate());
            txtBrand.setText(this.vehicle.getBrand());
            txtModel.setText(this.vehicle.getModel());
            txtType.setText(this.vehicle.getType().toString());
            txtYear.setText(String.valueOf(this.vehicle.getYear()));
            txtTariff.setText(String.valueOf(getTariffType()));
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
        java.awt.GridBagConstraints gridBagConstraints;

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
        getContentPane().setLayout(new java.awt.GridBagLayout());

        btnSearchR.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnSearchR.setText("Buscar Reserva");
        btnSearchR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchRActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 60;
        gridBagConstraints.gridy = 73;
        gridBagConstraints.gridwidth = 137;
        gridBagConstraints.ipadx = 133;
        gridBagConstraints.ipady = 90;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(74, 35, 0, 0);
        getContentPane().add(btnSearchR, gridBagConstraints);

        btnConfirm.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnConfirm.setText("Confirmar Reserva");
        btnConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 73;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.ipadx = 96;
        gridBagConstraints.ipady = 90;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(74, 21, 0, 0);
        getContentPane().add(btnConfirm, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Detalles de la reserva:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 18;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 16;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(jLabel1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Cedula:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 0, 0);
        getContentPane().add(jLabel2, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Placa:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 28;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 0, 0);
        getContentPane().add(jLabel3, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Inicia:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 20, 0, 0);
        getContentPane().add(jLabel4, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Finaliza:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 0, 0);
        getContentPane().add(jLabel5, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Tipo:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 28;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        getContentPane().add(jLabel6, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Fabricante:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 28;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        getContentPane().add(jLabel7, gridBagConstraints);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Modelo:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 28;
        gridBagConstraints.gridy = 32;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 36;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        getContentPane().add(jLabel8, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Año:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 28;
        gridBagConstraints.gridy = 69;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        getContentPane().add(jLabel9, gridBagConstraints);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Tarifa diaria:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 69;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 0, 0);
        getContentPane().add(jLabel10, gridBagConstraints);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Apariencia fisica del vehiculo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 71;
        gridBagConstraints.gridwidth = 55;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 26, 0, 0);
        getContentPane().add(jLabel11, gridBagConstraints);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/ToyotaCorolla.jpg"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 72;
        gridBagConstraints.gridwidth = 107;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 0, 0);
        getContentPane().add(jLabel12, gridBagConstraints);

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/GrupoDypurConjunto.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 75;
        gridBagConstraints.ipady = -14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jLabel13, gridBagConstraints);

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/BannerPartners.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 197;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 199;
        gridBagConstraints.gridheight = 75;
        gridBagConstraints.ipadx = -15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 25, 0, 1);
        getContentPane().add(jLabel14, gridBagConstraints);

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/GrupoDypurSlogan.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 198;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = -150;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 150, 0, 0);
        getContentPane().add(jLabel15, gridBagConstraints);

        txtYear.setEditable(false);
        txtYear.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtYear.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 29;
        gridBagConstraints.gridy = 69;
        gridBagConstraints.gridwidth = 136;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 232;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 0, 0);
        getContentPane().add(txtYear, gridBagConstraints);

        txtClient.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 13;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 162;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        getContentPane().add(txtClient, gridBagConstraints);

        txtTariff.setEditable(false);
        txtTariff.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtTariff.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 69;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 122;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 1, 0, 0);
        getContentPane().add(txtTariff, gridBagConstraints);

        txtType.setEditable(false);
        txtType.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtType.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 31;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 134;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 222;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 1, 0, 0);
        getContentPane().add(txtType, gridBagConstraints);

        txtBrand.setEditable(false);
        txtBrand.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtBrand.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 33;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 132;
        gridBagConstraints.gridheight = 17;
        gridBagConstraints.ipadx = 162;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        getContentPane().add(txtBrand, gridBagConstraints);

        txtModel.setEditable(false);
        txtModel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtModel.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 32;
        gridBagConstraints.gridy = 32;
        gridBagConstraints.gridwidth = 133;
        gridBagConstraints.gridheight = 37;
        gridBagConstraints.ipadx = 182;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 12, 0, 0);
        getContentPane().add(txtModel, gridBagConstraints);

        btnSearchV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/searchIcon.png"))); // NOI18N
        btnSearchV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchVActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 194;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 8;
        gridBagConstraints.ipadx = 102;
        gridBagConstraints.ipady = 31;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 30, 0, 0);
        getContentPane().add(btnSearchV, gridBagConstraints);

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel16.setText("Buscar vehiculo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 194;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 50, 0, 0);
        getContentPane().add(jLabel16, gridBagConstraints);

        txtFinalize.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy"))));
        txtFinalize.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 13;
        gridBagConstraints.gridheight = 35;
        gridBagConstraints.ipadx = 162;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 6, 0, 0);
        getContentPane().add(txtFinalize, gridBagConstraints);

        txtInitialize.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy"))));
        txtInitialize.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 15;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.ipadx = 182;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 10, 0, 0);
        getContentPane().add(txtInitialize, gridBagConstraints);

        txtPlate.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 31;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 134;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 222;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 0, 0);
        getContentPane().add(txtPlate, gridBagConstraints);

        btnModify.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnModify.setText("Modificar Reserva");
        btnModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModifyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 18;
        gridBagConstraints.gridy = 73;
        gridBagConstraints.gridwidth = 29;
        gridBagConstraints.ipadx = 101;
        gridBagConstraints.ipady = 90;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(74, 45, 0, 0);
        getContentPane().add(btnModify, gridBagConstraints);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/clearIcon.png"))); // NOI18N
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 8;
        gridBagConstraints.ipadx = 102;
        gridBagConstraints.ipady = 31;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 21, 0, 0);
        getContentPane().add(btnClear, gridBagConstraints);

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel17.setText("Limpiar datos");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 41, 0, 0);
        getContentPane().add(jLabel17, gridBagConstraints);

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
            frmSearch.loadTable();
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
