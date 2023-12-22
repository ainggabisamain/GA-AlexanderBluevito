/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.geneticalgorithm;

import com.example.geneticalgorithm.Genetic.Kromosom;
import com.opencsv.CSVWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Sumber Referensi :
 * - Membaca File : https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
 * - Print hasil genetic ke CSV : https://www.geeksforgeeks.org/writing-a-csv-file-in-java-using-opencsv/
 * 
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        pilihUkPuzzle(); //mengeluarkan menu pilihan mosaic puzzle yang tersedia
        int pilihanPuzzle = 0;//variabel untuk menyimpan input pilihan puzzle user yang pertamanya di set 0 karena belom dilakukan pemilihan oleh user
        try {
            pilihanPuzzle = sc.nextInt(); //input pilihan puzzle user
        } catch (InputMismatchException e) { //user memilih nomor menu yang tidak tersedia
            System.out.println("Input pilihan puzzle yang anda masukkan salah, silakan jalankan program lagi");
            return;
        }

        int ukPuzzle = ukPuzzle(pilihanPuzzle); //mengambil ukuran puzzle sebenarnya dari pilihan user
        System.out.println("");

        System.out.println("Silakan masukkan banyak test case yang ingin diuji: (ketik angkanya saja, ex: 5)"); //test case yang user ingin scrape langsung dari website mosaic uzzle
        int ukTestCase = 0;//variabel untuk menyimpan berapa banyak test case yang ingin diuji
        try {
            ukTestCase = sc.nextInt();//input banyak test case
        } catch (InputMismatchException e) {
            System.out.println("Input ukuran test case yang anda masukkan salah, silakan jalankan program lagi");
            return;
        }
        System.out.println("");

        System.out.println("Silakan masukkan banyak generasi: (ketik angkanya saja, ex: 20)"); //banyak generasi yang akan dipakai saat algoritma genetik dijalankan
        int generasi = 0;//variabel untuk menyimpan berapa banyak generasi yang akan digunakan di genetic algorithm
        try {
            generasi = sc.nextInt();//input jumlah generasi yang akan digunakan di genetic algorithm
        } catch (InputMismatchException e) { //user memasukkan input generasi dengan format yang salah. Misalnya input selain bilangan bulat
            System.out.println("Input generasi yang anda masukkan salah, silakan jalankan program lagi");
            return;
        }
        System.out.println("");

        System.out.println("Silakan masukkan banyak populasi: (ketik angkanya saja, ex: 1000)"); //banyak populasi yang akan digenerate ssat algoritma genetik dijalankan
        int populasi = 0;//variabel untuk menyimpan berapa banyak populasi yang akan digunakan di genetic algorithm
        try {
            populasi = sc.nextInt();//input jumlah populasi yang akan digunakan di genetic algorithm
        } catch (InputMismatchException e) {
            System.out.println("Input populasi yang anda masukkan salah, silakan jalankan program lagi");
            return;
        }
        System.out.println("");

        System.out.println("Silakan masukkan banyak elitism: (ketik angkanya saja, ex: 80)");
        int elitism = 0;//variabel untuk menyimpan berapa banyak elitism yang akan digunakan di genetic algorithm
        try {
            elitism = sc.nextInt();//input jumlah elitism yang akan digunakan di genetic algorithm
        } catch (InputMismatchException e) {
            System.out.println("Input elitism yang anda masukkan salah, silakan jalankan program lagi");
            return;
        }
        System.out.println("");

        int[][] mine = new int[ukPuzzle][ukPuzzle];//variabel untuk menyimpan kotak papan

        String home = System.getProperty("user.home"); //mengambil path dari user yang saat ini log in. Contoh bila di Windows maka "C:\Users\(nama user)\..."
        File file = new File(home + File.separator + "Documents" + File.separator + "PSC Java" + File.separator + "PSCVito" + File.separator + "geneticalgo" + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "com" + File.separator + "example" + File.separator + "geneticalgorithm" + File.separator + "testCaseAll.txt"); //file yang akan ditulis test case yang discrape dari web
        
        Scraper scraper = new Scraper(file, pilihanPuzzle); //instansiasi objek scraper
        scraper.execute(ukTestCase); //mulai scraping sebanya testcase yang diinginkan
        BufferedReader br = new BufferedReader(new FileReader(file)); //instansiasi objek buffered reader untuk menulis testcase ke dalam file .txt
        String[] testCaseLine = new String[ukTestCase]; //array sementara untuk menampung testcase
        for (int i = 0; i < ukTestCase; i++) {
            testCaseLine[i] = br.readLine(); //tulis test case ke dalam file .txt 
            System.out.println(testCaseLine[i]);
        }

        String[][] testCaseStr = new String[ukTestCase][]; //array untuk menampung test case yang nantinya akan diparse terlebih dahulu
        Integer[][] testCase = new Integer[ukTestCase][ukPuzzle * ukPuzzle]; //test case yang dapat digunakan oleh algoritma genetik

        for (int i = 0; i < ukTestCase; i++) {
            testCaseStr[i] = testCaseLine[i].replaceAll("[^0-9-]", " ").split(" ", ukPuzzle * ukPuzzle); //parse input yang discrape dari website. hilangkan semua char selain 0-9 dan - dan pisah berdasarkan spasinya

            for (int j = 0; j < ukPuzzle * ukPuzzle; j++) {
                testCase[i][j] = Integer.valueOf(testCaseStr[i][j].replace(" ", "")); //ubah string yang baru diparse menjadi integer. dan masukkan ke array testCase yang nantinya akan digunakan
            }
        }
        
        File fileCSV =  new File(home + File.separator + "Documents" + File.separator + "PSC Java" + File.separator + "PSCVito" + File.separator + "geneticalgo" + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "com" + File.separator + "example" + File.separator + "geneticalgorithm" + File.separator + "result.csv"); //file .csv untuk menampung hasil dari algoritma genetik. termasuk catatn populasi, elitism, fitness, generasi, dan waktu selama algoritma genetik dijalankan

        try {
            FileWriter outputfile = new FileWriter(fileCSV); //instansiasi objek FileWriter

            CSVWriter writer = new CSVWriter(outputfile); //instansiasi objek CSVWriter
            String soal = "";//variabel untuk menyimpan soal
            String[] header = {"Soal", "Hasil Genetic", "Populasi", "Generasi", "Elitism", "Fitnes", "Time"}; //header yang akan digunakan dalam file .csv
            writer.writeNext(header); //masukkan header
            for (int i = 0; i < ukTestCase; i++) { //parse test case menjadi bentuk tabel, seperti yang ditampilkan pada website
                int l = 0;//iterator untuk tiap angka dari test case karena di test case, input angka dalam 1 baris untuk 1 test case
                for (int j = 0; j < ukPuzzle; j++) {
                    for (int k = 0; k < ukPuzzle; k++) {
                        mine[j][k] = testCase[i][l];//memasukan isi kotak baris ke j kolom ke k
                        soal += testCase[i][l] + " ";//memasukkan isi testcase baris ke i kolom ke l ke soal
                        l++;
                    }
                    soal += "\n";//melakukan enter untuk pindah baris
                }
                
                Genetic gen = new Genetic(mine); //instansiasi objek algoritma genetik
                Kromosom res = gen.run_ga(populasi, generasi, elitism); //jalankan algoritma genetik dengan parameter populasi, generasi, dan elitism yang sudah ditentukan
                Integer[] hasil = res.getKromosom(); //algoritma genetik selesai dijalankan. hasil merupakan kromoson terbaik dari populasi
                double fitness = gen.getFitness(); //nilai fitness terbaik
                long time = gen.getTime();//waktu yang dibutuhkan
                System.out.println("populasi : " + populasi + " generasi: " + generasi + " elitism: " + elitism + " fitness " + fitness); //detail ditampilkan saat algoritma genetik selesai diajalankan
                System.out.println("fitness " + res.getFitness()); //fitness terbaik ditampilkan
                String jawaban = "";//variabel untuk menyimpan hasil jawaban dari genetic algorithm
                for (int m = 0; m < hasil.length; m++) { //hasil kromosom terbaik ditampilkan
                    System.out.print(hasil[m] + " ");
                    jawaban += hasil[m] + " ";//memasukan isi gene ke m ke jawaban
                }
                System.out.println("");
                String[] data = {soal,jawaban, Integer.toString(populasi), Integer.toString(generasi), Integer.toString(elitism), Double.toString(fitness), Long.toString(time)};//memasukan soal, jawaban genetic algorithm, populasi, generasi, elitism, fitness, dan waktu ke array untuk nanti dimasukkan ke csv          
                writer.writeNext(data); //detail dimasukkan ke file .csv       
                soal = "";//meng-set variabel soal menjadi string kosong
            }
            writer.close();//selesai melakukan penulisan
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void pilihUkPuzzle() {
        System.out.println("Silakan pilih dari ukuran puzzle berikut: (ketik angkanya saja, ex: 1)");
        System.out.println("1. 5x5 easy");
        System.out.println("2. 5x5 hard");
        System.out.println("3. 7x7 easy");
        System.out.println("4. 7x7 hard");
        System.out.println("5. 10x10 easy");
        System.out.println("6. 10x10 hard");
        System.out.println("7. 15x15 easy");
        System.out.println("8. 15x15 hard");
        System.out.println("9. 20x20 easy");
        System.out.println("10. 20x20 hard");
    }

    public static int ukPuzzle(int pilihan) {//method untuk mengeluarkan ukuran papan dari input pilihan dari user
        if (pilihan == 1 || pilihan == 2) {
            return 5;
        } else if (pilihan == 3 || pilihan == 4) {
            return 7;
        } else if (pilihan == 5 || pilihan == 6) {
            return 10;
        } else if (pilihan == 7 || pilihan == 8) {
            return 15;
        } else {//pilihan nomor 9 atau pilihan nomor 10
            return 20;
        }
    }
}
