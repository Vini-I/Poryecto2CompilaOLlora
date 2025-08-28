/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Contracts;

import Clients.Client;
import Clients.ClientManager;
import Exceptions.ContractAlreadyExistsException;
import Exceptions.InvalidDateException;
import Exceptions.NoCarSelectedException;
import Exceptions.NoClientException;
import Exceptions.OverlappingReservationException;
import GuiList.Clearable;
import GuiList.Requireable;
import GuiList.Saveable;
import GuiList.Searchable;
import GuiList.Showable;
import Reservations.Reservation;
import Reservations.ReservationList;
import Utils.UtilDate;
import Utils.UtilGui;
import Vehicles.Vehicle;
import Vehicles.VehicleManager;
import Vehicles.VehicleType;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Brwni
 */
public class FrmContract extends javax.swing.JFrame  implements Requireable, Saveable, Searchable, Showable, Clearable {
    private ContractList managerCt;
    private ClientManager managerCl;
    private VehicleManager managerV;
    private Vehicle vehicle;
    private Client client;
    private Contract contrato;
    
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    
    /**
     * Creates new form FrmContract
     */
    public FrmContract() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        initManagers();
        model = (DefaultTableModel) tblContracts.getModel();
        sorter = new TableRowSorter<>(model);
        tblContracts.setRowSorter(sorter);
    }
    
    private void initManagers() {
        managerCt = ContractList.getInstance();
        managerCl = new ClientManager();
        managerV = new VehicleManager();
    }
    
    @Override
    public boolean validateRequiere() {
        return UtilGui.validateRequiere(txtPlate, txtClient, txtFinalize);
    }
    
    @Override
    public void save() {
        
    }
    
    @Override
    public void search() {
        try {
            List<Contract> result;
            if (!txtClient.getText().isBlank()) {
                result = managerCt.getContractsByClientId(txtClient.getText());
            } else if (!txtPlate.getText().isBlank()) {
                result = managerCt.getContractsByVehiclePlate(txtPlate.getText());
            } else if (!txtContractNum.getText().isBlank()){
                result = managerCt.getContractByNumber(txtContractNum.getText());
            } else {
                result = managerCt.getAllContracts();
            }
            loadTable(result);
            if (result.isEmpty()) {
                UtilGui.showErrorMessage(this, "No se encontraron contratos.", "Sin Resultados");
            }
        } catch (Exception e) {
            UtilGui.showErrorMessage(this, "Error en la búsqueda: " + e.getMessage(), "Error");
        }
    }

    @Override
    public void showData() {
        if (this.contrato == null) {
            UtilGui.showErrorMessage(this, "No se ha seleccionado ningún contrato.", "Error");
            return;
        }
        
        txtContractNum.setText(contrato.getContractNum());
        txtClient.setText(contrato.getClient().getId());
        txtPlate.setText(contrato.getVehicle().getPlate());
        txtInitialize.setText(UtilDate.toString(contrato.getStartTime()));
        txtFinalize.setText(UtilDate.toString(contrato.getFinishTime()));
        txtBrand.setText(contrato.getVehicle().getBrand());
        txtModel.setText(contrato.getVehicle().getModel());
        txtType.setText(contrato.getVehicle().getType().toString());
        txtYear.setText(String.valueOf(contrato.getVehicle().getYear()));
        txtTariff.setText(String.valueOf(contrato.getTariff()));
        txtTotal.setText(String.valueOf(contrato.getTotalAmount()));
        txtContractState.setText(contrato.getState().toString());
    }
    
    public void showVehicleData(Vehicle vehicle) {
        if (vehicle != null) {
            txtBrand.setText(vehicle.getBrand());
            txtModel.setText(vehicle.getModel());
            txtType.setText(vehicle.getType().toString());
            txtYear.setText(String.valueOf(vehicle.getYear()));
        }
    }
    
    private TariffType getTariffType() {
        if (VehicleType.SEDAN.equals(txtType.getText())) return TariffType.SEDAN;
        else if (VehicleType.SUV.equals(txtType.getText())) return TariffType.SUV;
        else if (VehicleType.PICKUP.equals(txtType.getText())) return TariffType.PICKUP;
        else if (VehicleType.VAN.equals(txtType.getText())) return TariffType.VAN;
        else if (VehicleType.MINIVAN.equals(txtType.getText())) return TariffType.MINIVAN;
        else return TariffType.MINIBUS;
    }
    
    @Override
    public void clear() {
        txtClient.setText("");
        txtPlate.setText("");
        txtContractNum.setText("");
        txtClient.setText("");
        txtPlate.setText("");
        txtInitialize.setText("");
        txtFinalize.setText("");
        txtBrand.setText("");
        txtModel.setText("");
        txtType.setText("");
        txtYear.setText("");
        txtTariff.setText("");
        txtTotal.setText("");
        txtContractNum.setText("");
        txtContractState.setText("");
        
        loadTable(managerCt.getAllContracts());
    }
    
    private void loadTable(List<Contract> contractList) {
        model = (DefaultTableModel) tblContracts.getModel();
        model.setRowCount(0);
        
        for (Contract c : contractList) {
            Object[] row = {
                c.getContractNum(),
                c.getClient().getId(),
                c.getVehicle().getPlate(),
                c.getStartTime(),
                c.getFinishTime(),
                c.getTariff(),
                c.getTotalAmount()
            };
            model.addRow(row);
        }
    }
    
    public void searchVehicle() {
        String plate = txtPlate.getText();
        if (!plate.isBlank()) {
            try {
                this.vehicle = managerV.findVehicle(plate);

                if (this.vehicle != null) {
                    showVehicleData(this.vehicle);
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
    
    private void clearCarFields() {
        txtPlate.setText("");
        txtBrand.setText("");
        txtModel.setText("");
        txtType.setText("");
        txtYear.setText("");
        txtTariff.setText("");
        txtTotal.setText("");
    }
    
    public void add() {
        try {
            if (validateRequiere()) {
                UtilGui.showErrorMessage(this, "Por favor, llene todos los campos de información.", "Datos Requeridos");
                return;
            }

            client = managerCl.findClient(txtClient.getText());
            vehicle = managerV.findVehicle(txtPlate.getText());

            if (client == null) {
                UtilGui.showErrorMessage(this, "No se encontró un cliente con la cédula proporcionada.", "Cliente No Encontrado");
                return;
            }

            if (vehicle == null) {
                UtilGui.showErrorMessage(this, "No se encontró un vehículo con la placa proporcionada.", "Vehículo No Encontrado");
                return;
            }

            LocalDate startDate = UtilDate.toLocalDate(String.valueOf(txtInitialize.getText()));
            LocalDate endDate = UtilDate.toLocalDate(String.valueOf(txtFinalize.getText()));

            Reservation newReservation = ReservationList.getInstance().createReservation(client, vehicle, startDate, endDate);

            TariffType tariff = getTariffType(); 

            Contract newContract = new Contract(managerCt.getNextContractNumber(), newReservation, tariff);

            managerCt.addContract(newContract);

            UtilGui.showMessage(this, "Contrato agregado con éxito.", "Contrato Creado");

            txtClient.setText("");
            txtPlate.setText("");

            loadTable(managerCt.getAllContracts());

        } catch (ContractAlreadyExistsException | InvalidDateException | NoCarSelectedException | NoClientException | OverlappingReservationException e) {
            UtilGui.showErrorMessage(this, "Error al agregar el contrato: " + e.getMessage(), "Error");
        }
    }
    
    public void finalizeContract() {
        if (this.contrato == null) {
            UtilGui.showErrorMessage(this, "Debe seleccionar un contrato para finalizar.", "Error");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea finalizar este contrato?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            this.contrato.finalizeContract();
            UtilGui.showMessage(this, "El contrato ha sido finalizado con éxito.", "Contrato Finalizado");

            loadTable(managerCt.getAllContracts());
            showData();
        }
    }
    
    public void cancelContract() {
        if (this.contrato == null) {
            UtilGui.showErrorMessage(this, "Debe seleccionar un contrato para cancelar.", "Error");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea cancelar este contrato?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            this.contrato.cancelContract();
            UtilGui.showMessage(this, "El contrato ha sido cancelado.", "Contrato Cancelado");

            loadTable(managerCt.getAllContracts());
            showData();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtYear = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtTariff = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtFinalize = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        txtModel = new javax.swing.JTextField();
        txtBrand = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtType = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtInitialize = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtClient = new javax.swing.JTextField();
        txtPlate = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblContracts = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel17 = new javax.swing.JLabel();
        btnSearchV = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        btnClear = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtContractNum = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        btnFinalize = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        txtContractState = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtYear.setEditable(false);
        txtYear.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtYear.setEnabled(false);
        getContentPane().add(txtYear, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 490, 270, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Tarifa total:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 570, -1, -1));

        txtTariff.setEditable(false);
        txtTariff.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtTariff.setEnabled(false);
        getContentPane().add(txtTariff, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 530, 190, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Tarifa diaria:");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 530, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Finaliza:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, -1));

        txtFinalize.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy"))));
        txtFinalize.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        getContentPane().add(txtFinalize, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 290, 230, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Modelo:");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 450, -1, -1));

        txtModel.setEditable(false);
        txtModel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtModel.setEnabled(false);
        getContentPane().add(txtModel, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 450, 220, -1));

        txtBrand.setEditable(false);
        txtBrand.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtBrand.setEnabled(false);
        getContentPane().add(txtBrand, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 410, 200, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Fabricante:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, -1, -1));

        txtType.setEditable(false);
        txtType.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtType.setEnabled(false);
        getContentPane().add(txtType, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 370, 260, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Tipo:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, -1, -1));

        txtInitialize.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("dd/MM/yyyy"))));
        txtInitialize.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        getContentPane().add(txtInitialize, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 250, 250, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Numero:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Cedula:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        txtClient.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        getContentPane().add(txtClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 170, 230, -1));

        txtPlate.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        getContentPane().add(txtPlate, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 330, 260, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Placa:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, -1, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Lista de contratos:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 120, -1, -1));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Grupo Dypur.png"))); // NOI18N
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/GrupoDypurSlogan850x210.png"))); // NOI18N
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, -1, 110));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Grupo Dypur.png"))); // NOI18N
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 0, -1, -1));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/BannerPartners.png"))); // NOI18N
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(1330, 0, -1, 900));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Año:");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 490, -1, -1));

        txtTotal.setEditable(false);
        txtTotal.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtTotal.setEnabled(false);
        getContentPane().add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 570, 200, -1));

        tblContracts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Numero contrato", "Placa", "Hora de inicio", "Hora de finalizacion", "Tarifa", "Total a cobrar"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblContracts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblContractsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblContracts);
        if (tblContracts.getColumnModel().getColumnCount() > 0) {
            tblContracts.getColumnModel().getColumn(0).setResizable(false);
            tblContracts.getColumnModel().getColumn(1).setResizable(false);
            tblContracts.getColumnModel().getColumn(2).setResizable(false);
            tblContracts.getColumnModel().getColumn(3).setResizable(false);
            tblContracts.getColumnModel().getColumn(4).setResizable(false);
            tblContracts.getColumnModel().getColumn(5).setResizable(false);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 180, 920, 570));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        getContentPane().add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 110, 10, 790));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Detalles del contrato:");
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));

        btnSearchV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/searchIcon.png"))); // NOI18N
        btnSearchV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchVActionPerformed(evt);
            }
        });
        getContentPane().add(btnSearchV, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 700, 160, 60));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel16.setText("Buscar vehiculo");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 670, -1, -1));

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/clearIcon.png"))); // NOI18N
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        getContentPane().add(btnClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 700, 160, 60));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel18.setText("Limpiar datos");
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 670, -1, -1));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Inicia:");
        getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, -1, -1));

        txtContractNum.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        getContentPane().add(txtContractNum, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 210, 220, -1));

        btnAdd.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnAdd.setText("Agregar Contraro");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        getContentPane().add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 840, 340, 50));

        btnFinalize.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnFinalize.setText("Finalizar");
        btnFinalize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinalizeActionPerformed(evt);
            }
        });
        getContentPane().add(btnFinalize, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 770, 160, 60));

        btnCancel.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnCancel.setText("Cancelar");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        getContentPane().add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 770, 160, 60));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel20.setText("Estado:");
        getContentPane().add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 610, -1, -1));

        txtContractState.setEditable(false);
        txtContractState.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtContractState.setEnabled(false);
        getContentPane().add(txtContractState, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 610, 240, -1));

        btnSearch.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        btnSearch.setText("Buscar contrato");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        getContentPane().add(btnSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(379, 757, 770, 130));

        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/update2Icon.png"))); // NOI18N
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        getContentPane().add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 757, 140, 130));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchVActionPerformed
        searchVehicle();
    }//GEN-LAST:event_btnSearchVActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clear();
    }//GEN-LAST:event_btnClearActionPerformed

    private void tblContractsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblContractsMouseClicked
        int selectedRow = tblContracts.getSelectedRow();
        if (selectedRow != -1) {
            int modelRow = tblContracts.convertRowIndexToModel(selectedRow);

            String contractNum = (String) tblContracts.getModel().getValueAt(modelRow, 0);

            List<Contract> result = managerCt.getContractByNumber(contractNum);

            if (!result.isEmpty()) {
                this.contrato = result.get(0);
                showData();
            }
        }
    }//GEN-LAST:event_tblContractsMouseClicked
    
    public void refreshTable() {
        loadTable(managerCt.getAllContracts());
    }
    
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        add();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnFinalizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinalizeActionPerformed
        finalizeContract();
    }//GEN-LAST:event_btnFinalizeActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        cancelContract();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        search();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        refreshTable();
    }//GEN-LAST:event_btnRefreshActionPerformed

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
            java.util.logging.Logger.getLogger(FrmContract.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmContract.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmContract.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmContract.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmContract().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnFinalize;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSearch;
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
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable tblContracts;
    private javax.swing.JTextField txtBrand;
    private javax.swing.JTextField txtClient;
    private javax.swing.JTextField txtContractNum;
    private javax.swing.JTextField txtContractState;
    private javax.swing.JFormattedTextField txtFinalize;
    private javax.swing.JFormattedTextField txtInitialize;
    private javax.swing.JTextField txtModel;
    private javax.swing.JTextField txtPlate;
    private javax.swing.JTextField txtTariff;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtType;
    private javax.swing.JTextField txtYear;
    // End of variables declaration//GEN-END:variables

}
