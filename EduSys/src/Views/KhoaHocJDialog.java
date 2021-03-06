package Views;

import DAO.ChuyenDeDAO;
import DAO.KhoaHocDAO;
import Entity.ChuyenDe;
import Entity.KhoaHoc;
import utils.*;
import utils.XDate;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import java.awt.HeadlessException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author HP
 */
public class KhoaHocJDialog extends javax.swing.JDialog {

    KhoaHocDAO dao = new KhoaHocDAO();
    int row = -1;
    int index = 0;
    DefaultComboBoxModel cbo1;

    /**
     * Creates new form KhoaHocJDialog
     */
    public KhoaHocJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.fillComboBoxChuyenDe();
        init();
        pack();
    }

    ChuyenDeDAO cddao = new ChuyenDeDAO();

    void fillComboBoxChuyenDe() {
        List<ChuyenDe> list = cddao.selectAll();
        Vector v = new Vector();
        for (ChuyenDe cd : list) {
            v.add(cd.getMaCD());
        }
        cbo1 = new DefaultComboBoxModel(v);
        cboChuyenDe.setModel(cbo1);
    }

    void init() {
        Calendar cl = Calendar.getInstance();
        cl.setTime(XDate.toDate(XDate.now()));
        setTitle("EduSys - Quản lý khóa học");
        this.fillTable();
        lblTenCD.setText(cboChuyenDe.getSelectedItem().toString());
        this.row = -1;
        txtHocPhi.setEnabled(false);
        txtNguoiTao.setEnabled(false);
        txtThoiLuong.setEnabled(false);
        txtNgayTao.setEnabled(false);
        txtNgayTao.setText(String.valueOf(cl.get(Calendar.MONTH) + 1) + "/" + String.valueOf(cl.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(cl.get(Calendar.YEAR)));
        txtNguoiTao.setText(Auth.user.getMaNV());
        ChuyenDe cd = cddao.selectById(cboChuyenDe.getSelectedItem().toString());
        txtHocPhi.setText(String.valueOf(cd.getHocPhi()));
        txtThoiLuong.setText(String.valueOf(cd.getThoiLuong()));
        updateStatus(true);
        setLocationRelativeTo(null);
    }

    void fillTable() {
        DefaultTableModel model = (DefaultTableModel) tblKhoaHoc.getModel();
        model.setRowCount(0); //xóa tất cả các hàng trên jTable
        try {
            String maCD = cboChuyenDe.getSelectedItem().toString();
            List<KhoaHoc> list = dao.selectByChuyenDe(maCD);
            Calendar cl = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            for (KhoaHoc kh : list) {
                cl.setTime(XDate.toDate(kh.getNgayKG(), "yyyy-MM-dd"));
                c2.setTime(XDate.toDate(kh.getNgayTao(), "yyyy-MM-dd"));
                model.addRow(new Object[]{
                    kh.getMaKH(),
                    kh.getThoiLuong(),
                    kh.getHocPhi(),
                    String.valueOf(cl.get(Calendar.MONTH) + 1) + "/" + String.valueOf(cl.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(cl.get(Calendar.YEAR)),
                    //                    XDate.toString(kh.getNgayKG(),"MM/dd/yyyy"),
                    kh.getMaNV(),
                    //                    XDate.toString(kh.getNgayTao(),"MM/dd/yyyy"),
                    String.valueOf(c2.get(Calendar.MONTH) + 1) + "/" + String.valueOf(c2.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(c2.get(Calendar.YEAR)),});
                tblKhoaHoc.setModel(model);
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu");
        }
    }

    void chonChuyenDe() {
        ChuyenDe chuyenDe = (ChuyenDe) cboChuyenDe.getSelectedItem();
        txtThoiLuong.setText(String.valueOf(chuyenDe.getThoiLuong()));
        txtHocPhi.setText(String.valueOf(chuyenDe.getHocPhi()));
        lblTenCD.setText(chuyenDe.getTenCD());
        txtGhiChu.setText(chuyenDe.getTenCD());
        this.fillTable();
        this.row = -1;
        this.updateStatus(true);
        tabs.setSelectedIndex(1);
    }

    void setModel(KhoaHoc model) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(XDate.toDate(model.getNgayKG(), "yyyy-MM-dd"));
        lblTenCD.setText(model.getMaCD());
        txtHocPhi.setText(String.valueOf(model.getHocPhi()));
        txtNguoiTao.setText(model.getMaNV());
        txtNgayKG.setText(String.valueOf(cl.get(Calendar.MONTH) + 1) + "/" + String.valueOf(cl.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(cl.get(Calendar.YEAR)));
        txtThoiLuong.setText(String.valueOf(model.getThoiLuong()));
        cl.setTime(XDate.toDate(model.getNgayTao(), "yyyy-MM-dd"));
        txtNgayTao.setText(String.valueOf(cl.get(Calendar.MONTH) + 1) + "/" + String.valueOf(cl.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(cl.get(Calendar.YEAR)));
        txtGhiChu.setText(model.getGhiChu());
    }

    KhoaHoc getModel() {
        KhoaHoc model = new KhoaHoc();
        String maCD = cboChuyenDe.getSelectedItem().toString();
        model.setMaCD(maCD);
        Calendar cl = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
//        model.setNgayKG(XDate.toDate(txtNgayKG.getText()));
        cl.setTime(XDate.toDate(txtNgayKG.getText(), "MM/dd/yyyy"));
        model.setNgayKG(String.valueOf(cl.get(Calendar.MONTH) + 1) + "/" + String.valueOf(cl.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(cl.get(Calendar.YEAR)));

        model.setHocPhi(Double.valueOf(txtHocPhi.getText()));
        model.setThoiLuong(Integer.valueOf(txtThoiLuong.getText()));
        model.setGhiChu(txtGhiChu.getText());
        model.setMaNV(Auth.user.getMaNV());

//        model.setNgayTao(XDate.toDate(txtNgayTao.getText()));
        c2.setTime(XDate.toDate(XDate.now()));
        model.setNgayTao(String.valueOf(c2.get(Calendar.MONTH) + 1) + "/" + String.valueOf(c2.get(Calendar.DAY_OF_MONTH)) + "/" + String.valueOf(c2.get(Calendar.YEAR)));
        return model;
    }
//    void clearForm(){
//        KhoaHoc kh = new KhoaHoc();
//        this.setModal();
//        this.row = -1;
//        this.updateStatus();
//    }

    void edit() {
        try {

            String makh = tblKhoaHoc.getValueAt(this.row, 0).toString();
            KhoaHoc model = dao.selectById(makh);
            if (model != null) {
                this.setModel(model);
                this.updateStatus(false);

            }
        } catch (Exception e) {
            e.printStackTrace();
//            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    void first() {
        this.row = 0;
        this.edit();
    }

    void prev() {
        if (this.row > 0) {
            this.row--;
            this.edit();
        }
    }

    void next() {
        if (this.row < tblKhoaHoc.getRowCount() - 1) {
            this.row++;
            this.edit();
        }
    }

    void last() {
        this.row = tblKhoaHoc.getRowCount() - 1;
        this.edit();
    }

    void updateStatus(boolean insertable) {
        btnInsert.setEnabled(insertable);
        btnUpdate.setEnabled(!insertable);
        btnDelete.setEnabled(!insertable);
        boolean first = this.index > 0;
        boolean last = this.index < tblKhoaHoc.getRowCount() - 1;
        btnFisrt.setEnabled(!insertable && first);
        btnPrev.setEnabled(!insertable && first);
        btnLast.setEnabled(!insertable && last);
        btnNext.setEnabled(!insertable && last);
        if(row > 0){
            btnFisrt.setEnabled(true);
            btnPrev.setEnabled(true);
        }else{
            btnFisrt.setEnabled(false);
            btnPrev.setEnabled(false);
        }
        if(row < tblKhoaHoc.getRowCount() - 1){
            btnNext.setEnabled(true);
            btnLast.setEnabled(true);
        }else{
            btnNext.setEnabled(false);
            btnLast.setEnabled(false);
        }
        
    }

//    void load() {
//        DefaultTableModel model = (DefaultTableModel) tblKhoaHoc.getModel();
//        model.setRowCount(0);
//        try {
//            List<KhoaHoc> list = dao.selectAll();
//            for (KhoaHoc kh : list) {
//                Object[] row = {
//                    kh.getMaKH(),
//                    kh.getMaCD(),
//                    kh.getThoiLuong(),
//                    kh.getHocPhi(),
//                    XDate.toString(kh.getNgayKG()),
//                    kh.getMaNV(),
//                    XDate.toString(kh.getNgayTao())
//                };
//                model.addRow(row);
//            }
//        } catch (Exception e) {
//            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
//        }
//    }
    void insert() {
        KhoaHoc model = getModel();
        try {
            dao.insert(model);
            this.fillTable();
            MsgBox.alert(this, "Thêm mới thành công!");
        } catch (HeadlessException e) {
            MsgBox.alert(this, "Thêm mới thất bại!");
        }
    }

    void update() {
        KhoaHoc model = getModel();
        try {
            model.setMaKH(Integer.valueOf(tblKhoaHoc.getValueAt(this.row, 0).toString()));
            System.out.println(model.getGhiChu());
            dao.update(model);
            this.fillTable();
            MsgBox.alert(this, "Cập nhật thành công!");
        } catch (Exception e) {
            MsgBox.alert(this, "Cập nhật thất bại!");
        }
    }

    void delete() {
        if (MsgBox.confirm(this, "Bạn thực sự muốn xóa khóa học này?")) {
            String makh = tblKhoaHoc.getValueAt(this.row, 0).toString();
            try {
                dao.delete(makh);
                this.fillTable();
                this.clear();
                MsgBox.alert(this, "Xóa thành công!");
            } catch (Exception e) {
                MsgBox.alert(this, "Xóa thất bại!");
            }
        }
    }

    void clear() {
        tblKhoaHoc.clearSelection();
        this.row = -1;
        txtNgayKG.setText("");
        txtGhiChu.setText("");
        updateStatus(true);
    }

    boolean flag = false;

    void check() {
        if (txtThoiLuong.getText().equals("") || !txtHocPhi.getText().equals("")) {
            checktl();
        } else if (txtNgayKG.getText().equals("")) {
            MsgBox.alert(this, "Không bỏ trống ngày khai giảng!");
        } else if (txtNgayTao.getText().equals("")) {
            MsgBox.alert(this, "Không bỏ trống ngày tạo!");
        } else {
            flag = true;
        }
    }

    void checktl() {
        if (txtThoiLuong.getText().equals("0") || txtThoiLuong.getText().equals("")) {
            MsgBox.alert(this, "Không bỏ trống thời lượng!");
        } else if (txtHocPhi.getText().equals("") || txtHocPhi.getText().equals("0.0")) {
            MsgBox.alert(this, "Không bỏ trống học phí!");
        } else if (!txtThoiLuong.getText().equals("0") || !txtHocPhi.getText().equals("0.0")) {
            String hp = "java.lang.NumberFormatException: For input string: ";
            String tl2 = "java.lang.NumberFormatException: For input string: ";
            String tl = "";
            try {
                if (!txtThoiLuong.getText().equals("0") || !txtHocPhi.getText().equals("0.0")) {
                    int thoiluong = Integer.parseInt(txtThoiLuong.getText());
                    double hocphi = Double.parseDouble(txtHocPhi.getText());
                    if (thoiluong <= 0) {
                        MsgBox.alert(this, "Thời lượng là số dương và phải lớn hơn 0");
                    } else if (hocphi <= 0) {
                        MsgBox.alert(this, "Học phí là số dương và phải lớn hơn 0");
                    } else {
                        flag = true;
                    }
                }

            } catch (Exception e) {
                tl += e.toString();
                hp += "\"" + (txtHocPhi.getText()).toString() + "\"";
                tl2 += "\"" + (txtThoiLuong.getText()).toString() + "\"";
                System.out.println(hp);
                System.out.println(tl2);
                if (tl.equals(hp)) {
                    MsgBox.alert(this, "Học phí phải truyền vào kiểu số!");
                } else if (tl.equals(tl2)) {
                    MsgBox.alert(this, "Thời lượng phải truyền vào kiểu số!");
                }
            }
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

        jLabel1 = new javax.swing.JLabel();
        tabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtNgayKG = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtHocPhi = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtThoiLuong = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtNguoiTao = new javax.swing.JTextField();
        txtNgayTao = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtGhiChu = new javax.swing.JTextArea();
        btnInsert = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnFisrt = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        lblTenCD = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblKhoaHoc = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        cboChuyenDe = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 204));
        jLabel1.setText("CHUYÊN ĐỀ");

        jLabel3.setText("Ngày khai giảng (MM/dd/yyy):");

        jLabel4.setText("Học phí");

        jLabel5.setText("Thời lượng (giờ)");

        jLabel6.setText("Người tạo");

        jLabel7.setText("Ngày tạo (MM/dd/yyyy)");

        jLabel8.setText("Ghi chú");

        txtGhiChu.setColumns(20);
        txtGhiChu.setRows(5);
        jScrollPane1.setViewportView(txtGhiChu);

        btnInsert.setText("Thêm");
        btnInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertActionPerformed(evt);
            }
        });

        btnUpdate.setText("Sửa");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setText("Xóa");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnClear.setText("Mới");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnLast.setText(">|");
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        btnNext.setText(">");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnPrev.setText("<");
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnFisrt.setText("|<");
        btnFisrt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFisrtActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblTenCD.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTenCD.setForeground(new java.awt.Color(255, 0, 0));
        lblTenCD.setText("Chuyên đề");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(lblTenCD)
                .addGap(0, 222, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTenCD)
                .addContainerGap())
        );

        jLabel2.setText("Chuyên đề");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnInsert)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 133, Short.MAX_VALUE)
                        .addComponent(btnFisrt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPrev)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNext)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLast))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtNguoiTao, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtHocPhi, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel2))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNgayKG, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtThoiLuong)
                            .addComponent(txtNgayTao)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jLabel7))
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel3))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtNgayKG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtHocPhi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtThoiLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNguoiTao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInsert)
                    .addComponent(btnUpdate)
                    .addComponent(btnDelete)
                    .addComponent(btnClear)
                    .addComponent(btnLast)
                    .addComponent(btnNext)
                    .addComponent(btnPrev)
                    .addComponent(btnFisrt)))
        );

        tabs.addTab("CẬP NHẬT", jPanel1);

        tblKhoaHoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã KH", "Thời lượng", "Học phí", "Khai giảng", "Tạo bởi", "Ngày tạo"
            }
        ));
        tblKhoaHoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblKhoaHocMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblKhoaHoc);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabs.addTab("DANH SÁCH", jPanel2);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        cboChuyenDe.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cboChuyenDe.setForeground(new java.awt.Color(255, 0, 0));
        cboChuyenDe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1" }));
        cboChuyenDe.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboChuyenDeItemStateChanged(evt);
            }
        });
        cboChuyenDe.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                cboChuyenDeComponentHidden(evt);
            }
        });
        cboChuyenDe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboChuyenDeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cboChuyenDe, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cboChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboChuyenDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboChuyenDeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboChuyenDeActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        clear();
        btnInsert.setEnabled(true);
//        Date ngaymua = new Date();
//        SimpleDateFormat DATE_FORMATER = new SimpleDateFormat("MM/dd/yyyy");
//        String ngay2 = DATE_FORMATER.format(ngaymua);
//
//        String[] key = ngay2.split("/");
//        int ngay = Integer.parseInt(key[0]);
//        int thang = Integer.parseInt(key[1]);
//        String time = "" + (ngay + 1) + "/" + key[1] + "/" + key[2] + "";
//        String time1;

//        try {
//
////            Date date = DATE_FORMATER.parse(time);
////            String ngay3 = DATE_FORMATER.format(date);
////            txtNgayKG.setText(ngay3);
////            txtNgayTao.disable();
////            txtNgayTao.setText(ngay2);
////            txtNguoiTao.setText(Auth.user.getMaNV());
//        } catch (ParseException ex) {
//
//        }
    }//GEN-LAST:event_btnClearActionPerformed

    private void tblKhoaHocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKhoaHocMouseClicked
        // TODO add your handling code here:
        this.row = tblKhoaHoc.getSelectedRow();
        this.edit();
        tabs.setSelectedIndex(0);
    }//GEN-LAST:event_tblKhoaHocMouseClicked

    private void btnInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertActionPerformed
        // TODO add your handling code here:
        check();
        if (flag == true) {
            try {
                String ngay11 = txtNgayKG.getText().strip();
//                System.out.println(ngay11);
                Date date = XDate.toDate(ngay11, "MM/dd/yyyy");
                if (!date.after(new Date())) {
                    MsgBox.alert(this, "Ngày khai giảng phai sau ngày hiện tại!");
                    return;
                } else {
                    insert();
                }
            } catch (Exception e) {
                e.printStackTrace();
                MsgBox.alert(this, "Sai định dạng ngày!");
            }
        }

    }//GEN-LAST:event_btnInsertActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        check();
        if (flag == true) {
            try {
                String ngay11 = txtNgayKG.getText();
                Date date = XDate.toDate(ngay11, "MM/dd/yyyy");
                if (!date.after(new Date())) {
                    MsgBox.alert(this, "Ngày khai giảng phai sau ngày hiện tại!");
                    return;
                } else {
                    update();
                }
            } catch (Exception e) {
                MsgBox.alert(this, "Sai định dạng ngày!");
            }
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        delete();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnFisrtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFisrtActionPerformed
        // TODO add your handling code here:
        if(this.row > 0){
            this.row = 0;
            tblKhoaHoc.setRowSelectionInterval(this.row, this.row);
            this.edit();
            updateStatus(false);
        }
    }//GEN-LAST:event_btnFisrtActionPerformed

    private void cboChuyenDeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboChuyenDeItemStateChanged
        // TODO add your handling code here:
        lblTenCD.setText(cboChuyenDe.getSelectedItem().toString());
        ChuyenDe cd = cddao.selectById(cboChuyenDe.getSelectedItem().toString());
        txtHocPhi.setText(String.valueOf(cd.getHocPhi()));
        txtThoiLuong.setText(String.valueOf(cd.getThoiLuong()));
        fillTable();
        clear();
    }//GEN-LAST:event_cboChuyenDeItemStateChanged

    private void cboChuyenDeComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_cboChuyenDeComponentHidden
        // TODO add your handling code here:
    }//GEN-LAST:event_cboChuyenDeComponentHidden

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        // TODO add your handling code here:
        if(this.row > 0){
            this.row--;
            tblKhoaHoc.setRowSelectionInterval(this.row, this.row);
            this.edit();
            updateStatus(false);
        }
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        if(this.row < tblKhoaHoc.getRowCount() - 1){
            this.row++;
            tblKhoaHoc.setRowSelectionInterval(this.row, this.row);
            this.edit();
            updateStatus(false);
        }
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        // TODO add your handling code here:
        if(this.row < tblKhoaHoc.getRowCount() - 1){
            this.row = tblKhoaHoc.getRowCount() - 1;
            tblKhoaHoc.setRowSelectionInterval(this.row, this.row);
            this.edit();
            updateStatus(false);
        }
    }//GEN-LAST:event_btnLastActionPerformed

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
            java.util.logging.Logger.getLogger(KhoaHocJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(KhoaHocJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(KhoaHocJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(KhoaHocJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                KhoaHocJDialog dialog = new KhoaHocJDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFisrt;
    private javax.swing.JButton btnInsert;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cboChuyenDe;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblTenCD;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblKhoaHoc;
    private javax.swing.JTextArea txtGhiChu;
    private javax.swing.JTextField txtHocPhi;
    private javax.swing.JTextField txtNgayKG;
    private javax.swing.JTextField txtNgayTao;
    private javax.swing.JTextField txtNguoiTao;
    private javax.swing.JTextField txtThoiLuong;
    // End of variables declaration//GEN-END:variables

}
