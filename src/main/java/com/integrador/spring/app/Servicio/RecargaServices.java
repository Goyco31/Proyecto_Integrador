package com.integrador.spring.app.Servicio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.integrador.spring.app.DAO.ComprarMonedasRepo;
import com.integrador.spring.app.DAO.RecargaRepo;
import com.integrador.spring.app.DAO.UserDAO;
import com.integrador.spring.app.Modelo.ComprarMonedas;
import com.integrador.spring.app.Modelo.Recarga;
import com.integrador.spring.app.Modelo.Recarga.Estado;
import com.integrador.spring.app.Modelo.Recarga.TipoPago;
import com.integrador.spring.app.Modelo.User;

@Service
public class RecargaServices {
    @Autowired
    private RecargaRepo repo_recarga;

    @Autowired
    private UserDAO repo_user;

    @Autowired
    private ComprarMonedasRepo repo_compra;

    public List<Recarga> listarRecarga() {
        return repo_recarga.findAll();
    }

    public ResponseEntity<String> recargar(Integer idUser, Integer idCompra) {
        Optional<User> existeUser = repo_user.findById(idUser);
        Optional<ComprarMonedas> existeOpcion = repo_compra.findById(idCompra);
        Recarga nuevaRecarga = new Recarga();

        if (existeUser.isEmpty() || existeOpcion.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("error");
            // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User usuario = existeUser.get();
        ComprarMonedas comprarMonedas = existeOpcion.get();

        usuario.setMonedas(usuario.getMonedas() + comprarMonedas.getCantidad());
        repo_user.save(usuario);

        nuevaRecarga.setUsuario(usuario);
        nuevaRecarga.setComprarMonedas(comprarMonedas);
        nuevaRecarga.setTipoPago(TipoPago.PAYPAL);
        nuevaRecarga.setEstado(Estado.CANCELADO);
        repo_recarga.save(nuevaRecarga);

        return ResponseEntity.ok("Recarga realizada");
        // return new ResponseEntity<>(HttpStatus.OK);
    }

    public Recarga guardar(Recarga recarga) {
        return repo_recarga.save(recarga);
    }

    public void eliminar(int id) {
        repo_recarga.deleteById(id);
    }

    public byte[] exportRecargasToExcel() throws IOException {
    List<Recarga> recargas = repo_recarga.findAll();

    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Historial de Recargas");

    // Estilo para el encabezado
    CellStyle headerStyle = workbook.createCellStyle();
    headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    Font headerFont = workbook.createFont();
    headerFont.setBold(true);
    headerStyle.setFont(headerFont);

    // Crear fila de encabezado (CORRECCIÓN: agregar índice de columna en createCell)
    Row headerRow = sheet.createRow(0);
    String[] headers = {"ID", "Usuario", "Cantidad de Monedas", "Tipo de Pago", "Estado", "Fecha"};
    for (int i = 0; i < headers.length; i++) {
        Cell cell = headerRow.createCell(i); // ¡Faltaba el índice "i" aquí!
        cell.setCellValue(headers[i]);
        cell.setCellStyle(headerStyle);
    }

    // Llenar datos (CORRECCIÓN: paréntesis y nombres de métodos)
    int rowNum = 1;
    for (Recarga recarga : recargas) { // Corregido: usé llaves "{" en lugar de corchetes "["
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(recarga.getIdRecarga()); // Usar getId() en lugar de getIdRecarga()
        row.createCell(1).setCellValue(recarga.getUsuario().getNombre());
        row.createCell(2).setCellValue(recarga.getComprarMonedas().getCantidad()); // Corregido: getComprarMonedas()
        row.createCell(3).setCellValue(recarga.getTipoPago().toString());
        row.createCell(4).setCellValue(recarga.getEstado().toString());
        row.createCell(5).setCellValue(recarga.getFechaRecarga().toString());
    }

    // Autoajustar columnas
    for (int i = 0; i < headers.length; i++) {
        sheet.autoSizeColumn(i);
    }

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    workbook.write(outputStream);
    workbook.close();
    return outputStream.toByteArray();
}
}
