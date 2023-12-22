/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.geneticalgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Keterangan Sumber Referensi :
 * - Slide kuliah 05-06-ComplexSearch
 * - Buku Rishal Hurbans - Grokking Artificial Intelligence Algorithms (2020, Manning Publications)
 * - method generateIntialPopulation (Sumber : Buku Rishal Hurbans - Grokking Artificial Intelligence Algorithms (2020, Manning Publications) halaman 109)
 * - method set_probabilities_of_population (Sumber : Buku Rishal Hurbans - Grokking Artificial Intelligence Algorithms (2020, Manning Publications) halaman 116)
 * - method runRouletteWheel (Sumber : Buku Rishal Hurbans - Grokking Artificial Intelligence Algorithms (2020, Manning Publications) halaman 116)
 * - method spinRouletteWheel (Sumber : Buku Rishal Hurbans - Grokking Artificial Intelligence Algorithms (2020, Manning Publications) halaman 116)
 * - method createRouletteWheel (Sumber : Buku Rishal Hurbans - Grokking Artificial Intelligence Algorithms (2020, Manning Publications) halaman 116)
 * - method spinRankSelection (Sumber : Buku Rishal Hurbans - Grokking Artificial Intelligence Algorithms (2020, Manning Publications) halaman 116)
 * - method createRankSelection (Sumber : Buku Rishal Hurbans - Grokking Artificial Intelligence Algorithms (2020, Manning Publications) halaman 116)
 * - method runRankSelection (Sumber : Buku Rishal Hurbans - Grokking Artificial Intelligence Algorithms (2020, Manning Publications) halaman 116)
 * - method mutation (Sumber : Buku Rishal Hurbans - Grokking Artificial Intelligence Algorithms (2020, Manning Publications) halaman 122)
 * - method run_ga (Sumber : Buku Rishal Hurbans - Grokking Artificial Intelligence Algorithms (2020, Manning Publications) halaman 125)
 * - method mergeElitismAndChildren (Sumber : Buku Rishal Hurbans - Grokking Artificial Intelligence Algorithms (2020, Manning Publications) halaman 125)
 * - method runTournamentSelection : https://github.com/Apress/genetic-algorithms-in-java-basics/blob/master/GA%20in%20Java/src/chapter3/GeneticAlgorithm.java
 * - Mengurutkan HashMap by value : https://howtodoinjava.com/java/sort/java-sort-map-by-values/
 * 
 * 
 */
public class Genetic {
    Random rd;///variabel untuk membuat angka random
    private int[][] mineSweeper;//array untuk menyimpan isi minesweeper
    private int ukMineSweeper;//atribut untuk menyimpan ukuran berapa dari Mine sweeper (bisa berupa jumlah baris / kolom)
    private int jmlhChildGagalCrossOver;//atribut untuk menyimpan jumlah child yang gagal dibuat pada crossover//atribut untuk menyimpan jumlah child yang gagal dibuat pada crossover
    private long startTime;//atribut untuk menyimpan waktu mulai algoritma genetic dimulai dalam nano detik//atribut untuk menyimpan waktu mulai algoritma genetic dimulai dalam nano 
    private long endTime;//atribut untuk menyimpan waktu selesai algoritma genetic dimulai nano detik//atribut untuk menyimpan waktu selesai algoritma genetic dimulai nano 
    private double bestFitness;//atribut untuk menyimpan jumlah fitness terbaik
    private int ukPopulasi;//atribut untuk menyimpan jumlah populasi//atribut untuk menyimpan jumlah fitness terbaik
    
    public Genetic(int[][] mineSweeper){//constructor kelas Genetic dengan parameter isi minesweepernya//constructor kelas Genetic
        this.mineSweeper = mineSweeper;//memindahkan parameter isi minesweeper ke atribut
        this.ukMineSweeper = mineSweeper.length;//mengambil ukuran dari Minesweeper dan memindahkan ke atribut ukMineSweeper
        this.jmlhChildGagalCrossOver = 0;//inisialisasi atribut untuk menyimpan jumlah child yang gagal dibuat pada crossover dengan 0 karena pada awalnya yang gagal 0 karena algoritma genetic belum dijalankan//inisialisasi atribut untuk menyimpan jumlah child yang gagal dibuat pada crossover dengan 0 karena pada awalnya yang gagal 0 karena algoritma genetic belum dijalankan
        this.startTime = 0;//inisialisasi waktu mulai dengan 0 karena belum dimulai//inisialisasi waktu mulai dengan 0 karena belum dimulai
        this.endTime = 0;//inisialisasi waktu selesai dengan 0 karena belum dimulai
        this.rd = new Random ();//menginisialisasikan atribut random dengan kelas Random
        this.rd.setSeed(Integer.MAX_VALUE);//meng-set atribut random dengan seed//inisialisasi waktu selesai dengan 0 karena belum dimulai
        
    }
    
    class Kromosom{//kelas kromosom untuk menyimpan isi kromosom dan fitnessnya//kelas kromosom

        private Integer[] kromosom;//atribut kromosom untuk menyimpan kromosom//atribut kromosom untuk menyimpan kromosom
        private double fitness;//atribut fitness untuk menyimpan fitness dari atribut kromosom//atribut fitnes untuk menyimpan fitnes dari atribut kromosom
        
        public Kromosom(Integer[] kromosom, double fitness){//constructor kelas kromosom dengan parameter kromosom (array of integer) dan nilai fitnessnya
            this.kromosom = kromosom;//menginisialisasikan atribut kromosom dengan kromosom dari parameter
            this.fitness = fitness;//menginisialisasikan atribut fitness dengan fitness dari parameter
        }
        
        public Integer[] getKromosom() {//method untuk mendapatkan isi kromosom//method untuk mendapatkan kromosom
            return kromosom;//mengembalikan isi kromosom//mengembalikan kromosom
        }

        public void setKromosom(Integer[] kromosom, double fitness) {//method untuk meng-set kromosom dengan parameternya adalah kromosomnya dan nilai fitnessnya//method untuk meng-set kromosom dengan parameternya adalah kromosomnya dengan tipe data array of integer
            this.kromosom = kromosom;//mengganti value atribut kromosom dengan kromosom dari parameter
            this.fitness = fitness;//mengganti value atribut fitness dengan fitness dari parameter//mengganti value atribut kromosom dengan kromosom dari parameter
        }

        public double getFitness() {//method untuk mendapatkan fitness dari kromosom
            return fitness;//mengembalikan fitness
        }

        public void setFitness(double fitness){//method untuk mengganti nilai fitness dari kromosom dengan parameter nilai fitnessnya//method untuk meng-set value dari atribut fitness dengan fitness dari parameter dengan tipe data fitness adalah double
            this.fitness = fitness;//mengganti nilai dari atribut fitness dengan nilai fitness dari parameter//mengganti value atribut fitness dengan fitness dari parameter
        }
        
        public Integer getAlele(int idx){//method untuk mendapatkan isi gene (alele) dari kromosom pada index tertentu dengan parameternya adalah indexnya
            return this.kromosom[idx];//mengembalikan isi gene (alele) pada kromosom di index tertentu
        }
        
        public void setAlele(int idx, Integer value){//menggnati isi gene (alele) pada suatu kromosom di index tertentu dengan niali tertentu
            this.kromosom[idx] = value;//mengganti isi gene (alele) pada kromosom pada index ke idx (index dari parameter) dengan value dari parameter
        }
        
        
    }
    
    
    public Kromosom[] generateIntialPopulation(int ukPopulation){//method untuk meng-generate initial population dengan parameter jumlah populasinya (ukPopulation) 
        Kromosom[] population = new Kromosom[ukPopulation];//array untuk menampung populasi yaitu semua individu / kromosom
        this.ukPopulasi = ukPopulation;//meng-assign atribut ukPopulasi dengan ukuran populasi dari parameter (ukPopulation)
        for(int i = 0; i < ukPopulation;i++){//looping untuk membuat kromosom / individu
            Integer[] kromosom = new Integer[this.ukMineSweeper*this.ukMineSweeper];//array untuk menyimpan kromosom sementara (untuk menyimpan alele)
            for(int j = 0; j < this.ukMineSweeper*this.ukMineSweeper;j++){//looping untuk membuat alele / mengisi setiap gene dari kromosom
                int alele = rd.nextInt(2);//membuat gene dengan melakukan random antara 0/1
                kromosom[j] = alele;//memasukkan alele ke kromoson ke index ke j
                
            }
            //Kromosom kromosomNew = new Kromosom(kromosom, this.fitnessFunction(kromosom));//membuat object dari kelas Kromosom yaitu untuk menyimpan kromosom dan fitensnya dengan memanggi method newFitnessFunction (fitness function yang lama)
            Kromosom kromosomNew = new Kromosom(kromosom, this.newFitnessFunction(kromosom));//membuat object dari kelas Kromosom yaitu untuk menyimpan kromosom dan fitensnya dengan memanggi method newFitnessFunction (fitness function yang baru)
            population[i] = kromosomNew;//memasukkan kromosom ke populasi
            
        }
        return population;//mengembalikan populasi yang telah di buat
    }
    
    public double fitnessFunction(Integer[] kromosom){//method untuk menghitung fitness (fitness function yg lama) dari kromosom (yang dimana komentar yang menunjukan baris atau kolom dilihat dimulai dari 1 bukan 0)
        int jmlhKotakKurang = 0;//variabel untuk menyimpan jumlah kotak hitam yang kurang dari seharusnya
        int jmlhKotakLebih = 0;//variabel untuk menyimpan jumlah kotak hitam yang berlebih dari yang seharusnya
        int jmlhKotakBelumBenar = 0;//variabel untuk menyimpan jumlah kotak yang belum benar (belum memenuhi jumlah kotak hitam dan kotak putih nya)
        for(int i = 0; i < this.ukMineSweeper;i++){//looping baris
            for(int j = 0; j < this.ukMineSweeper;j++){//looping kolom
                int isi = this.mineSweeper[i][j];//mengambil isi dari baris ke i, kolom ke j
                if(isi != -1){//jika baris ke i, kolom ke j ada isinya / angka
                    if(i == 0){//ada di baris paling atas
                        if(j == 0){//ada di kolom paling kiri
                            int jmlhHitam = 0;//inisialisasi jumlah kotak hitam dengan 0, saat variabel untuk menyimpan jumlah kotak hitam dibuat
                            if(kromosom[0] == 0){//cek baris paling atas, kolom paling kiri hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[1] == 0){//cek baris paling atas, kolom kedua dari kiri hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[(this.ukMineSweeper*1)] == 0){//cek baris kedua kolom paling kiri hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[(this.ukMineSweeper*1) + 1] == 0){//cek baris kedua kolom kedua dari kiri hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            
                            if(jmlhHitam > isi){//jika jumlah kotak hitam di sekitar kotak baris ke i kolom ke j lebih besar dari yang diperbolehkan (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakLebih += jmlhHitam-isi;//menambahkan jumlah kotak hitam berlebih dari yang seharusnya ke variabel tempat menyimpan jumlah kotak hitam yang berlebih
                            }
                            else if (jmlhHitam < isi){//jika jumlah kotak hitam di sekitar kotak baris ke i kolom ke j lebih kecil dari yang diperbolehkan (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakKurang += isi - jmlhHitam;//menambahkan jumlah kotak hitam yang kurang dari yang seharusnya ke variabel tempat menyimpan jumlah kotak hitam yang kurang
                            }
                            
                            if(jmlhHitam > isi || jmlhHitam < isi){//jika jumlah kotak hitam tidak sama dengan yang seharusnya (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakBelumBenar++;//menambahkan 1 ke variabel jmlhKotakBelumBenar karena kotak ini (kotak baris ke i kolom ke j) belum benar
                            }
                            
                        }
                        else if (j == (this.ukMineSweeper-1)){//ada di kolom paling kanan
                            int jmlhHitam = 0;//inisialisasi jumlah kotak hitam dengan 0, saat variabel untuk menyimpan jumlah kotak hitam dibuat
                            if(kromosom[this.ukMineSweeper-2] == 0){//cek baris paling atas, kolom kedua dari kanan hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper-1] == 0){//cek baris paling atas, kolom paling kanan hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[(2*this.ukMineSweeper)-2] == 0){//cek baris kedua, kolom kedua dari kanan hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[(2*this.ukMineSweeper)-1] == 0){//cek baris kedua, kolom paling kanan hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            
                            if(jmlhHitam > isi){//jika jumlah kotak hitam di sekitar kotak baris ke i kolom ke j lebih besar dari yang diperbolehkan (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakLebih += jmlhHitam-isi;//menambahkan jumlah kotak hitam berlebih dari yang seharusnya ke variabel tempat menyimpan jumlah kotak hitam yang berlebih
                            }
                            else if (jmlhHitam < isi){//jika jumlah kotak hitam di sekitar kotak baris ke i kolom ke j lebih kecil dari yang diperbolehkan (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakKurang += isi - jmlhHitam;//menambahkan jumlah kotak hitam yang kurang dari yang seharusnya ke variabel tempat menyimpan jumlah kotak hitam yang kurang
                            }
                            
                            if(jmlhHitam > isi || jmlhHitam < isi){//jika jumlah kotak hitam tidak sama dengan yang seharusnya (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakBelumBenar++;//menambahkan 1 ke variabel jmlhKotakBelumBenar karena kotak ini (kotak baris ke i kolom ke j) belum benar
                            }
                        }
                        else{//ada di kolom tengah
                            int jmlhHitam = 0;//inisialisasi jumlah kotak hitam dengan 0, saat variabel untuk menyimpan jumlah kotak hitam dibuat
                            if(kromosom[j-1] == 0){//cek baris paling atas, kolom ke j hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[j] == 0){//cek baris paling atas, kolom ke j+1 hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[j+1] == 0){//cek baris paling atas, kolom ke j+2 hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper+j-1] == 0){//cek baris kedua dari atas, kolom ke j hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper+j] == 0){//cek baris kedua dari atas, kolom ke j+1 hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper+j+1] == 0){//cek baris kedua dari atas, kolom ke j+2 hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            
                            if(jmlhHitam > isi){//jika jumlah kotak hitam di sekitar kotak baris ke i kolom ke j lebih besar dari yang diperbolehkan (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakLebih += jmlhHitam-isi;//menambahkan jumlah kotak hitam berlebih dari yang seharusnya ke variabel tempat menyimpan jumlah kotak hitam yang berlebih
                            }
                            else if (jmlhHitam < isi){//jika jumlah kotak hitam di sekitar kotak baris ke i kolom ke j lebih kecil dari yang diperbolehkan (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakKurang += isi - jmlhHitam;//menambahkan jumlah kotak hitam yang kurang dari yang seharusnya ke variabel tempat menyimpan jumlah kotak hitam yang kurang
                            }
                            
                            if(jmlhHitam > isi || jmlhHitam < isi){//jika jumlah kotak hitam tidak sama dengan yang seharusnya (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakBelumBenar++;//menambahkan 1 ke variabel jmlhKotakBelumBenar karena kotak ini (kotak baris ke i kolom ke j) belum benar
                            }
                            
                        }
                    }
                    else if(i == (this.ukMineSweeper-1)){//ada di baris paling bawah
                        if(j == 0){//ada di kolom paling kiri
                             int jmlhHitam = 0;//inisialisasi jumlah kotak hitam dengan 0, saat variabel untuk menyimpan jumlah kotak hitam dibuat
                             if(kromosom[this.ukMineSweeper*(i-1)] == 0){//cek baris kedua dari bawah, kolom paling kiri hitam atau bukan
                                 jmlhHitam++;//menambahkan jumlah kotak hitam
                             }
                             if(kromosom[this.ukMineSweeper*(i-1) + 1] == 0){//cek baris kedua dari bawah, kolom kedua dari kiri hitam atau bukan
                                 jmlhHitam++;//menambahkan jumlah kotak hitam
                             }
                             if(kromosom[this.ukMineSweeper*i] == 0){//cek baris paling bawah, kolom paling kiri hitam atau bukan
                                 jmlhHitam++;//menambahkan jumlah kotak hitam
                             }
                             if(kromosom[this.ukMineSweeper*1 + 1] == 0){//cek baris paling bawah, kolom kedua dari kiri hitam atau bukan
                                 jmlhHitam++;//menambahkan jumlah kotak hitam
                             }
                             
                             if(jmlhHitam > isi){//jika jumlah kotak hitam di sekitar kotak baris ke i kolom ke j lebih besar dari yang diperbolehkan (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakLebih += jmlhHitam-isi;//menambahkan jumlah kotak hitam berlebih dari yang seharusnya ke variabel tempat menyimpan jumlah kotak hitam yang berlebih
                             }
                             else if (jmlhHitam < isi){//jika jumlah kotak hitam di sekitar kotak baris ke i kolom ke j lebih kecil dari yang diperbolehkan (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakKurang += isi - jmlhHitam;//menambahkan julah kotak hitam yang kurang dari yang seharusnya ke variabel tempat menyimpan jumlah kotak hitam yang kurang
                             }
                             
                             if(jmlhHitam > isi || jmlhHitam < isi){//jika jumlah kotak hitam tidak sama dengan yang seharusnya (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakBelumBenar++;//menambahkan 1 ke variabel jmlhKotakBelumBenar karena kotak ini (kotak baris ke i kolom ke j) belum benar
                            }
                        }
                        else if (j == (this.ukMineSweeper-1)){//ada di kolom paling kanan
                             int jmlhHitam = 0;//inisialisasi jumlah kotak hitam dengan 0, saat variabel untuk menyimpan jumlah kotak hitam dibuat
                             if(kromosom[this.ukMineSweeper*(i-1) + j - 1] == 0){//cek baris kedua dari bawah, kolom kedua dari kanan hitam atau bukan
                                 jmlhHitam++;//menambahkan jumlah kotak hitam
                             }
                             if(kromosom[this.ukMineSweeper*(i-1) + j] == 0){//cek baris kedua dari bawah, kolom paling kanan hitam atau bukan
                                 jmlhHitam++;//menambahkan jumlah kotak hitam
                             }
                             if(kromosom[this.ukMineSweeper*i + j - 1] == 0){//cek baris paling bawah, kolom kedua dari kanan hitam atau bukan
                                 jmlhHitam++;//menambahkan jumlah kotak hitam
                             }
                             if(kromosom[this.ukMineSweeper*i + j] == 0){//cek baris paling bawah, kolom paling kanan hitam atau bukan
                                 jmlhHitam++;//menambahkan jumlah kotak hitam
                             }
                             
                             if(jmlhHitam > isi){//jika jumlah kotak hitam di sekitar kotak baris ke i kolom ke j lebih besar dari yang diperbolehkan (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakLebih += jmlhHitam-isi;//menambahkan jumlah kotak hitam berlebih dari yang seharusnya ke variabel tempat menyimpan jumlah kotak hitam yang berlebih
                             }
                             else if (jmlhHitam < isi){//jika jumlah kotak hitam di sekitar kotak baris ke i kolom ke j lebih kecil dari yang diperbolehkan (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakKurang += isi - jmlhHitam;//menambahkan jumlah kotak hitam yang kurang dari yang seharusnya ke variabel tempat menyimpan jumlah kotak hitam yang kurang
                             }
                             
                             if(jmlhHitam > isi || jmlhHitam < isi){//jika jumlah kotak hitam tidak sama dengan yang seharusnya (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakBelumBenar++;//menambahkan 1 ke variabel jmlhKotakBelumBenar karena kotak ini (kotak baris ke i kolom ke j) belum benar
                            }
                        }
                        else{//ada di kolom tengah
                            int jmlhHitam = 0;//inisialisasi jumlah kotak hitam dengan 0, saat variabel untuk menyimpan jumlah kotak hitam dibuat
                            if(kromosom[this.ukMineSweeper*(i-1) + j -1] == 0){//cek baris kedua dari bawah, kolom ke j hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper*(i-1) + j] == 0){//cek baris kedua dari bawah, kolom ke j+1 hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper*(i-1) + j + 1] == 0){//cek baris kedua dari bawah, kolom ke j+2 hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper*i + j - 1] == 0){//cek baris paling bawah, kolom ke j hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper*i + j] == 0){//cek baris paling bawah, kolom ke j+1 hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper*1 + j + 1] == 0){//cek baris paling bawah, kolom ke j+2 hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            
                            if(jmlhHitam > isi){//jika jumlah kotak hitam di sekitar kotak baris ke i kolom ke j lebih besar dari yang diperbolehkan (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakLebih += jmlhHitam-isi;//menambahkan jumlah kotak hitam berlebih dari yang seharusnya ke variabel tempat menyimpan jumlah kotak hitam yang berlebih
                             }
                             else if (jmlhHitam < isi){//jika jumlah kotak hitam di sekitar kotak baris ke i kolom ke j lebih kecil dari yang diperbolehkan (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakKurang += isi - jmlhHitam;//menambahkan julmah kotak hitam yang kurang dari yang seharusnya ke variabel tempat menyimpan jumlah kotak hitam yang kurang
                             }
                            
                            if(jmlhHitam > isi || jmlhHitam < isi){//jika jumlah kotak hitam tidak sama dengan yang seharusnya (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakBelumBenar++;//menambahkan 1 ke variabel jmlhKotakBelumBenar karena kotak ini (kotak baris ke i kolom ke j) belum benar
                            }
                        }
                    }
                    else{//ada di baris di tengah
                        if(j == 0){//ada di kolom paling kiri
                            int jmlhHitam = 0;//inisialisasi jumlah kotak hitam dengan 0, saat variabel untuk menyimpan jumlah kotak hitam dibuat
                            if(kromosom[this.ukMineSweeper*(i-1) + j] == 0){//cek baris i, kolom paling kiri hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper*(i-1) + j + 1] == 0){//cek baris i, kolom kedua dari kiri hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper*i + j] == 0){//cek baris ke i+1, kolom paling kiri hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper*i + j + 1] == 0){//cek baris i+1, kolom kedua dari kiri hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper*(i+1) + j] == 0){//cek baris ke i+2, kolom paling kiri hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper*(i+1) + j + 1] == 0){//cek baris i+2, kolom kedua dari kiri hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            
                            if(jmlhHitam > isi){//jika jumlah kotak hitam di sekitar kotak baris ke i kolom ke j lebih besar dari yang diperbolehkan (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakLebih += jmlhHitam-isi;//menambahkan jumlah kotak hitam berlebih dari yang seharusnya ke variabel tempat menyimpan jumlah kotak hitam yang berlebih
                             }
                             else if (jmlhHitam < isi){//jika jumlah kotak hitam di sekitar kotak baris ke i kolom ke j lebih kecil dari yang diperbolehkan (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakKurang += isi - jmlhHitam;//menambahkan jumlah kotak hitam yang kurang dari yang seharusnya ke variabel tempat menyimpan jumlah kotak hitam yang kurang
                             }
                            
                            if(jmlhHitam > isi || jmlhHitam < isi){//jika jumlah kotak hitam tidak sama dengan yang seharusnya (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakBelumBenar++;//menambahkan 1 ke variabel jmlhKotakBelumBenar karena kotak ini (kotak baris ke i kolom ke j) belum benar
                            }
                                
                        }
                        else if (j == (this.ukMineSweeper-1)){//ada di kolom paling kanan
                            int jmlhHitam = 0;//inisialisasi jumlah kotak hitam dengan 0, saat variabel untuk menyimpan jumlah kotak hitam dibuat
                            if(kromosom[this.ukMineSweeper*(i-1) + j-1] == 0){//cek baris ke i, kolom kedua dari kanan hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper*(i-1) + j] == 0){//cek baris ke i, kolom paling kanan hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper*i + j-1] == 0){//cek baris ke i+1, kolom kedua dari kanan hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper*i + j] == 0){//cek baris ke i+1, kolom paling kanan hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper*(i+1) + j-1] == 0){//cek baris ke i+2, kolom kedua dari kanan hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper*(i+1) + j] == 0){//cek baris ke i+2, kolom paling kanan hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            
                            if(jmlhHitam > isi){//jika jumlah kotak hitam di sekitar kotak baris ke i kolom ke j lebih besar dari yang diperbolehkan (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakLebih += jmlhHitam-isi;//menambahkan jumlah kotak hitam berlebih dari yang seharusnya ke variabel tempat menyimpan jumlah kotak hitam yang berlebih
                             }
                             else if (jmlhHitam < isi){//jika jumlah kotak hitam di sekitar kotak baris ke i kolom ke j lebih kecil dari yang diperbolehkan (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakKurang += isi - jmlhHitam;//menambahkan jumlah kotak hitam yang kurang dari yang seharusnya ke variabel tempat menyimpan jumlah kotak hitam yang kurang
                             }
                            
                            if(jmlhHitam > isi || jmlhHitam < isi){//jika jumlah kotak hitam tidak sama dengan yang seharusnya (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakBelumBenar++;//menambahkan 1 ke variabel jmlhKotakBelumBenar karena kotak ini (kotak baris ke i kolom ke j) belum benar
                            }
                        }
                        else{//ada di kolom tengah
                            int jmlhHitam = 0;//inisialisasi jumlah kotak hitam dengan 0, saat variabel untuk menyimpan jumlah kotak hitam dibuat
                            if(kromosom[this.ukMineSweeper*(i-1) + j-1] == 0){//cek baris ke i, kolom ke j hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper*(i-1) + j] == 0){//cek baris ke i, kolom ke j+1 hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper*(i-1) + j+1] == 0){//cek baris ke i, kolom ke j+2 hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper*i + j-1] == 0){//cek baris ke i+1, kolom ke j hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper*i + j] == 0){//cek baris ke i+1, kolom ke j+1 hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper*i + j+1] == 0){//cek baris ke i+1, kolom ke j+2 hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper*(i+1) + j-1] == 0){//cek baris ke i+2, kolom ke j hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper*(i+1) + j] == 0){//cek baris ke i+2, kolom ke j+1 hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            if(kromosom[this.ukMineSweeper*(i+1) + j+1] == 0){//cek baris ke i+2, kolom ke j+2 hitam atau bukan
                                jmlhHitam++;//menambahkan jumlah kotak hitam
                            }
                            
                            
                            if(jmlhHitam > isi){//jika jumlah kotak hitam di sekitar kotak baris ke i kolom ke j lebih besar dari yang diperbolehkan (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakLebih += jmlhHitam-isi;//menambahkan jumlah kotak hitam berlebih dari yang seharusnya ke variabel tempat menyimpan jumlah kotak hitam yang berlebih
                            }
                            else if (jmlhHitam < isi){//jika jumlah kotak hitam di sekitar kotak baris ke i kolom ke j lebih kecil dari yang diperbolehkan (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakKurang += isi - jmlhHitam;//menambahkan jumlah kotak hitam yang kurang dari yang seharusnya ke variabel tempat menyimpan jumlah kotak hitam yang kurang
                            }
                            
                            if(jmlhHitam > isi || jmlhHitam < isi){//jika jumlah kotak hitam tidak sama dengan yang seharusnya (isi dari kotak baris ke i kolom ke j)
                                jmlhKotakBelumBenar++;//menambahkan 1 ke variabel jmlhKotakBelumBenar karena kotak ini (kotak baris ke i kolom ke j) belum benar
                            }
                                
                        }
                    }
                }
            }
        }
        
        double fitnessValue = (0.3000 * jmlhKotakKurang) + (0.3000 * jmlhKotakLebih) + (0.400 * jmlhKotakBelumBenar);//menghitung nilai fitness dengan memperhatikan 3 fitur yaitu jumlah kotak hitam yang kurang dari seharusnya dan jumlah kotak hitam yang berlebih dari seharusnya dan jumlah kotak yang belum benar yang masing-masing memiliki bobot 30%, 30%, dan 40%
        return fitnessValue;//mengembalikan nilai fitness dari kromosom yang ada
    }
    
    
    
 
    
    public Kromosom[] mutation(Kromosom[] population, double mutationRate){//method untuk melakukan mutasi (Bit String) dengan parameternya adalah populasi dan mutation ratenya
        double totalMutation = population.length * mutationRate;//variabel untuk menyimpan berapa banyak individu/kromosom yang dilakukan mutasi pada populasi dengan mutation rate dari parameter
        for(int i = 0; i < totalMutation;i++){//looping untuk melakukan mutasi kromosom
            int chosenKromosom = rd.nextInt(population.length);//memilih individu yang akan dilakukan mutasi secara random dengan menggunakan atibut random dengan batas random sampai banyak kromosom/individu pada populasi
            int chosenGeneToBeMutate = rd.nextInt(this.ukMineSweeper*this.ukMineSweeper);//memilih gene yang akan dilakukan mutasi secara random dengan menggunakan atibut random dengan batas random sampai ukuran kromosom
            Kromosom kromosom = population[chosenKromosom];//mengambil kromosom yang akan dilakukan mutasi dari hasil random
            int alele = kromosom.getAlele(chosenGeneToBeMutate);//mengambil alele pada kromosom yang telah dipilih dengan random
            if(alele == 1 ){//jika alele adalah 1
                kromosom.setAlele(chosenGeneToBeMutate, 0);//mengganti alele pada gene yang telah dipilih pada kromosom yang telah dipilih dengan random dengan 0
                kromosom.setFitness(this.newFitnessFunction(kromosom.getKromosom()));//mengganti nilai fitnessnya dengan nilai fitenss yang baru (karena baru di mutasi) dengan fitness function newFitnessFunction
                //kromosom.setFitness(this.fitnessFunction(kromosom.getKromosom()));//mengganti nilai fitnessnya dengan nilai fitenss yang baru (karena baru di mutasi) dengan fitness function fitnessFunction
                
            }
            else{//jika alele adalah 0
                kromosom.setAlele(chosenGeneToBeMutate, 1);//mengganti alele pada gene yang telah dipilih pada kromosom yang telah dipilih dengan random dengan 1
                kromosom.setFitness(this.newFitnessFunction(kromosom.getKromosom()));//mengganti nilai fitnessnya dengan nilai fitenss yang baru (karena baru di mutasi) dengan fitness function newFitnessFunction
                //kromosom.setFitness(this.fitnessFunction(kromosom.getKromosom()));//mengganti nilai fitnessnya dengan nilai fitenss yang baru (karena baru di mutasi) dengan fitness function fitnessFunction
                
            }
            population[chosenKromosom] = kromosom;//mengganti kromosom yang dipilih untuk dimutasi pada populasi dengan kromosom yang sudah di mutasi
        }
        return population;//mengembalikan populasi dengan kromosom yang sudah di mutasi
    }
    
    public Kromosom[] mutationFlip(Kromosom[] population, double mutationRate){//method untuk melakukan mutasi (flip bit) dengan parameter populasi dan  mutation ratenya
        double totalMutation = population.length * mutationRate;//variabel untuk menyimpan berapa banyak individu/kromosom yang dilakukan mutasi pada populasi dengan mutation rate dari parameter
        for(int i = 0; i < totalMutation;i++){//looping untuk melakukan mutasi kromosom
            int chosenKromosom = rd.nextInt(population.length);//memilih individu yang akan dilakukan mutasi secara random dengan menggunakan atibut random dengan batas random sampai banyak kromosom/individu pada populasi
            Kromosom kromosom = population[chosenKromosom];//mengambil kromosom yang akan dilakukan mutasi dari hasil random
            for(int j = 0; j < this.ukMineSweeper*this.ukMineSweeper;j++){//looping untuk mengganti isi setiap gene dari kromosom yang telah dipilih dengan random
                if(kromosom.getAlele(j) == 1){//jika isi gene ke j pada kromosom yang telah dipilih adalah 1
                    kromosom.setAlele(j, 0);//mengganti isi gene ke j pada kromosom yang telah dipilih menjadi 0
                    
                }
                else{//jika isi gene ke j pada kromosom yang telah dipilih adalah 0
                    kromosom.setAlele(j, 1);//mengganti isi gene ke j pada kromosom yang telah dipilih menjadi 1
                    
                }
            }
            
            kromosom.setFitness(this.newFitnessFunction(kromosom.getKromosom()));//mengganti nilai fitnessnya dengan nilai fitenss yang baru (karena baru di mutasi) dengan fitness function newFitnessFunction
            //kromosom.setFitness(this.fitnessFunction(kromosom.getKromosom()));//mengganti nilai fitnessnya dengan nilai fitenss yang baru (karena baru di mutasi) dengan fitness function fitnessFunction
            
            population[chosenKromosom] = kromosom;//mengganti kromosom yang telah dipilih dari random pada populasi dengan kromosom yang sudah di mutasi
        }
        return population;//mengembalikan populasi dengan kromosom yang sudah di mutasi
    }
    
    
    
    public Kromosom[] twoPointCrossover(Kromosom parent1, Kromosom parent2, int point1, int point2){//method untuk melakukan two point crossover dengan parameter 2 kromosom yang akan dilakukan crossover dan 2 titik point untuk memisahkan bagian yang akan di crossover//method untuk melakukan two point crossover dengan parameter 2 kromosom yang akan dilakukan crossover dan 2 titik point untuk memisahkan bagian yang akan di crossover
        Integer[] child1 = new Integer[this.ukMineSweeper*this.ukMineSweeper];//membuat array of integer untuk menyimpan anak 1 hasil crossover//membuat array of integer untuk menyimpan anak 1 hasil crossover
        Integer[] child2 = new Integer[this.ukMineSweeper*this.ukMineSweeper];//membuat array of integer untuk menyimpan anak 2 hasil crossover//membuat array of integer untuk menyimpan anak 2 hasil crossover
        for(int i = 0; i < point1;i++){//looping untuk mengisi gene kromosom child 1 dan 2 dari index ke 0 sampai point1 - 1 dengan masing-masing parent 1 dan parent 2//looping untuk mengisi kromosom child 1 dan 2 dari index ke 0 sampai point1 - 1 dengan parent nya
            child1[i] = parent1.getAlele(i);//mengisi gene ke i pada child 1 dengan gene ke i dari parent 1//mengisi gene ke i pada child 1 dengan gene ke i dari parent 1
            child2[i] = parent2.getAlele(i);//mengisi gene ke i pada child 2 dengan gene ke i dari parent 2//mengisi gene ke i pada child 2 dengan gene ke i dari parent 2
        }
        
        for(int i = point1; i < point2; i++ ){//looping untuk menukar bagian kromosom dari point 1 sampai point 2 antara parent 1 dan parent 2//looping untuk menukar 
            child1[i] = parent2.getAlele(i);//menukar bagian gene ke i dari kromosom parent 2 ke child 1 dengan memasukkannya ke child 1
            child2[i] = parent1.getAlele(i);//menukar bagian gene ke i dari kromosom parent 1 ke child 2 dengan memasukkannya ke child 2
        }
        
        for(int i = point2; i < this.ukMineSweeper*this.ukMineSweeper;i++){//looping untuk mengisi gene kromosom child 1 dan 2 dari index ke point 2 sampai ke panjang parent-1 dengan masing-masing parent 1 dan parent 2
            child1[i] = parent1.getAlele(i);//mengisi gene ke i pada child 1 dengan gene ke i dari parent 1
            child2[i] = parent2.getAlele(i);//mengisi gene ke i pada child 2 dengan gene ke i dari parent 2
        }
        
        Kromosom[] children = new Kromosom[2];//membuat array of Kromosom untuk menampung 2 anak hasil crossover sehingga bisa di return kedua anak hasil crossover
        children[0] = new Kromosom(child1, this.newFitnessFunction(child1));//mengisi children pada index ke 0 dengan child 1 dengan tipe data Kromosom yang terlebih dahulu di inisialisasi dulu dengan parameter kromosomnya dan nilai fitnessnya dengan fitness function newFitnessFunction
        children[1] = new Kromosom(child2, this.newFitnessFunction(child2));;//mengisi children pada index ke 1 dengan child 2 dengan tipe data Kromosom yang terlebih dahulu di inisialisasi dulu dengan parameter kromosomnya dan nilai fitnessnya dengan fitness function newFitnessFunction
        //children[0] = new Kromosom(child1, this.fitnessFunction(child1));//mengisi children pada index ke 0 dengan child 1 dengan tipe data Kromosom yang terlebih dahulu di inisialisasi dulu dengan parameter kromosomnya dan nilai fitnessnya dengan fitness function fitnessFunction
        //children[1] = new Kromosom(child2, this.fitnessFunction(child2));;//mengisi children pada index ke 1 dengan child 2 dengan tipe data Kromosom yang terlebih dahulu di inisialisasi dulu dengan parameter kromosomnya dan nilai fitnessnya dengan fitness function fitnessFunction

        return children;//mengembalikan anak hasil crossover
    }
    
    public Kromosom[] crossOver(Kromosom[] parent, int crossOverRate){//method untuk melakukan crossover dari semua parent dengan crossover rate tertentu (sesuai parameter)
        ArrayList<Kromosom> childrenAll = new ArrayList<>();//membuat array list dengan tipe data Kromosom untuk menyimpan anak hasil crossover
        
        for(int i = 0; i < parent.length;i++){//looping untuk melakukan crossover dari semua parent 
                if(rd.nextDouble() <= (crossOverRate/100.0)){//melakukan random untuk menentukan apakah kromosom ini akan dilakukan crossover atau tidak yaitu jika hasil random dibawah atau sama dengan crossOverRate/100.0 maka akan dilakukan crossover
                    if(i+1 == parent.length){//jika tersisa 1 parent yang belum di crossover
                    childrenAll.add(parent[i]);//langsung menambahkan parent ini ke array list childrenAll (tanpa dilakukan crossover)
                    //break;
                    }
                    else{//jika jumlah parent tidak tersisa 1 / masih ada 2 parent sehingga masih bisa di crossover
                        Kromosom[] offSpring = twoPointCrossover(parent[i], parent[i+1], (this.ukMineSweeper*this.ukMineSweeper)/4, 3*(this.ukMineSweeper*this.ukMineSweeper)/4);//melakukan crossover dengan two point crossover dengan memanggil method twoPointCrossover dengan masukkan parameter 2 parent yang akan dilakukan crossoer dan titik pemotongannya yaitu titik point 1 = panjang kromosom/4 dan titik point 2 = 3 * panjang kromosom / 4
                        childrenAll.add(offSpring[0]);//menambahkan hasil anak pertama dari hasil crossover ke array list childrenAll untuk disimpan
                        childrenAll.add(offSpring[1]);//menambahkan hasil anak kedua dari hasil crossover ke array list childrenAll untuk disimpan
                    }
                }
                else{//hasil nilai random lebih dari crossOverRate/100.0 maka gagal melakukan crossover
                    this.jmlhChildGagalCrossOver += 2;//menambahkan 2 ke atribut jmlhChildGagalCrossOver karena 2 anak gagal dihasilkan dari hasil crossover
                }
                i++;//menambahkan i dengan 1 karena melakukan crossover dari 2 parent jadi sudah melewatkan 2 kromosom sehingga parent yang sudah di crossover tidak dilakukan crossover lagi
        }
        Kromosom[] selectedChildren = new Kromosom[childrenAll.size()];//membuat array of Kromosom dengan panjang dari ukuran variabel childrenAll (banyak anak yang berhasil di crossover) untuk digunakan sebagai return hasil anak yang berhasil di crossover
        
        for(int i = 0; i < childrenAll.size();i++){//melakukan looping array list childrenAll untuk memindahkan setiap kromosom di setiap index ke array of Integer 
            selectedChildren[i] = childrenAll.get(i);//memindahkan kromsom dari array list pada index ke i ke array of Integer 
        }
        
        return selectedChildren;//mengembalikan hasil anak dari hasil crossover yang berhasil dilakukan
    }
    
    public Kromosom[] mergeElitismAndChildren(Kromosom[] population, Kromosom[] children, int elitismRate){//method untuk menggambungkan anak hasil crossover dan elitism untuk dijadikan populasi baru
        Kromosom[] mergeResult = new Kromosom[this.ukPopulasi];//membuat array of Kromosom untuk menyimpan hasil merge (untuk populasi yang baru)
        
        LinkedHashMap<Kromosom, Double> sortedPopulation = this.sortingKromosom(population);//mengurutkan kromosom pada populasi bedasarkan hasil fitness
 
        
        
        int j = 0;//variabel untuk iterator sebagai index untuk menempatkan kromosom di array mergeResult
        for(j = 0; j < children.length;j++){//looping untuk memindahkan isi array children ke array mergeResult (array hasil merge)  
            mergeResult[j] = children[j];//memindahkan kromosom pada index ke j dari array children ke array mergeResult pada index ke j
            
        }
        
        
        for (HashMap.Entry<Kromosom, Double> et : sortedPopulation.entrySet()) {//looping untuk memindahkan kromosom terbaik (elitism) ke array mergeResult
            if(j < population.length){//jika iterator j masih kurang dari banyak populasi 
                mergeResult[j] = et.getKey();//memindahkan kromosom ke array mergeResult pada index ke j 
                j++;//menambahkan 1 ke iterator j karena sudah menambahkan 1 kromosom ke array mergeResult
            }
            else{//jika iterator j sudah sama dengan banyak populasi (array mergeResult sudah terisi penuh / jumlah populasi baru sama dengan jumlah populasi pertama)
                break;//keluar dari looping
            }
        }
        
        
        
        return mergeResult;//mengembalikan kromosom yang sudah di merge antara anak dengan elitism
           
    }
    
    public LinkedHashMap<Kromosom, Double> sortingKromosom(Kromosom[] kromosom){//method untuk mengurutkan kromosom bedasarkan nilai fitness dengan parameter array of kromoosm
        HashMap<Kromosom,Double> fitnessPopulation = new HashMap<Kromosom, Double>();//membuat hash map dengan tipe data key adalah kromosom dan tipe data value adalah double yang digunakan untuk menyimpan kromosom dengan fitness nya agar bisa diurutkan bedasarkan fitness
        for(int i = 0; i < kromosom.length;i++){//looping untuk memindahkan isi parameter kromosom ke hash map
            fitnessPopulation.put(kromosom[i], kromosom[i].getFitness());//memasukkan kromosom ke i ke hash map 
        }
        LinkedHashMap<Kromosom, Double> sortedPopulation = new LinkedHashMap<>();//membuat linked hash map untuk menyimpan kromosom yang sudah terurut bedasarkan nilai fitnessnya dengan tipe data key adalah kromosom dan tipe data value adalah double
        //fitnessPopulation.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(x -> sortedPopulation.put(x.getKey(), x.getValue()));//mengurutkan fitness dari kecil ke besar (ascending)
        fitnessPopulation.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) .forEachOrdered(x -> sortedPopulation.put(x.getKey(), x.getValue()));//mengurutkan fitness dari besar ke kecil (ascending)
        return sortedPopulation;//mengembalikan kromosom yang sudah terurut bedasarkan nilai fitness
    }
    
    public Kromosom run_ga(int ukPopulation, int jmlhGenerasi, int elitismRate){//method untuk menjalankan genetic algorithm
        this.startTime = System.nanoTime();//mengambil waktu saat ini (sebagai waktu mulai algoritma geentic dijalankan) dengan format nano detik
        //double bestFitness = Integer.MAX_VALUE;//membuat variabel bestFitness untuk menyimpan nilai fitness terbaik sekarang yang pertamanya di set nilai infinite karena algoritma genetic belom dijalankan (untuk fitness function fitnessFunction)
        double bestFitness = Integer.MIN_VALUE;//membuat variabel bestFitness untuk menyimpan nilai fitness terbaik sekarang yang pertamanya di set nilai negative infinite karena algoritma genetic belom dijalankan (untuk fitness function newFitnessFunction)
        Kromosom[] populasi = generateIntialPopulation(ukPopulation);//membuat populasi dengan memanggil method generateIntialPopulation dengan parameter adalah banyak kromosom pada suatu populasi
        Kromosom result = bestKromosom(populasi);//membuat variabel untuk menyimpan kromosom terbaik untuk populasi saat ini yang dilihat dengan nilai fitnessnya yang pertama kali di set dari kromosom terbaik dari populasi pertama
        for(int generasi = 1; generasi <= jmlhGenerasi; generasi++){//looping untuk re-generasi 
            double currentFitness = bestFitness(populasi);//mencari nilai fitness terbaik untuk populasi saat ini
            
//            if(currentFitness < bestFitness){//Buat fitness function yang fitnessFunction -> jika fitness terbaik sekarang lebih kecil dari best fitness
//                bestFitness = currentFitness;//mengganti best fitness dengan fitness terbaik sekarang
//                result = bestKromosom(populasi);//mengganti kromosom terbaik dengan kromosom terbaik sekarang 
//                
//            }
            
            if(currentFitness > bestFitness){//Buat fitness function yang newFitnessFunction -> jika fitness terbaik sekarang lebih besar dari best fitness
                bestFitness = currentFitness;//mengganti best fitness dengan fitness terbaik sekarang
                result = bestKromosom(populasi);//mengganti kromosom terbaik dengan kromosom terbaik sekarang
                
            }
            
            Kromosom[] parent;//membuat array of Kromosom untuk menyimpan parent
            //parent = runRouletteWheel(elitismRate, populasi);//membuat parent dengan roulette wheel
            parent = runTournamentSelection(elitismRate,populasi);//membuat parent dengan tournament selection
            //parent = runRankSelection(elitismRate,populasi);//membuat parent dengan rank selection

                

            
            Kromosom[] children = crossOver(parent,80);//membuat children dari parent yang terpilih dengan teknik two point crossover dengan crossover rate 80%
            
            
            Kromosom[] mutateChildren = mutation(children, 0.001);//melakukan mutasi dari anak hasil crossover dengan mutation rate 0.001 dengan teknik mutasi bit string
            //Kromosom[] mutateChildren = mutationFlip(children,0.001);//melakukan mutasi dari anak hasil crossover dengan mutation rate 0.001 dengan teknik mutasi flip bit
            
            
            

            populasi = mergeElitismAndChildren(populasi,mutateChildren,elitismRate);//menggabungkan hasil anak yang sudah dimutasi dengan elitism dengan elitism rate sesuai parameter

            
        }
        
        
        this.endTime = System.nanoTime();//mendapatkan waktu sekarang (sebagai waktu selesai algoritma geentic dijalankan) dengan format nano detik
        this.bestFitness = bestFitness;//meng-set atribut best fitness dengan best fitness dari keseluruhan generasi (sejak algoritma genetic dijalankan)
        
        return result;//mengembalikan kromosom yang terbaik
    }
    
    public double bestFitness(Kromosom[] population ){//method untuk menghitung nilai fitness terbaik pada populasi parameter
        //double best = Integer.MAX_VALUE;//fitness terbaik sekarang dengan di set nilai infinite karena pencarian belum dilakukan (untuk fitness function fitnessFunction)
        double best = Integer.MIN_VALUE;//fitness terbaik sekarang dengan di set nilai negatif infinite karena pencarian belum dilakukan (untuk fitness function newFitnessFunction)
        for(int i = 0; i < population.length;i++){//looping untuk mencari fitness terbaik pada populasi parameter
            double fitness = population[i].getFitness();//mengambil nilai fitness dari kromosom pada index ke i dari populasi parameter
            if(fitness > best){//jika fitness sekarang lebih besar dari fitness terbaik (untuk fitness function newFitnessFunction)
                best = fitness;//mengganti best (tempat menyimpan fitness terbaik) dengan niali fitness dari kromosom ke i
            }

            
//            if(fitness < best){//jika fitness sekarang lebih kecil dari fitness terbaik sekarang  (untuk fitness function fitnessFunction)
//                best = fitness;//mengganti best (tempat menyimpan fitness terbaik) dengan niali fitness dari kromosom ke i
//            }
        }
        return best;//mengembalikan nilai fitness terbaik pada populasi parameter
    }
    
    public Kromosom bestKromosom(Kromosom[] population){//method untuk mencari kromosom terbaik (kromosom yang memiliki nilai fitnes terbaik) dari populasi parameter
        //double best = Integer.MAX_VALUE;//fitness terbaik sekarang dengan di set nilai infinite karena pencarian belum dilakukan (untuk fitness function fitnessFunction)
        double best = Integer.MIN_VALUE;//fitness terbaik sekarang dengan di set nilai negatif infinite karena pencarian belum dilakukan (untuk fitness function newFitnessFunction)
        Kromosom bestKromosom = population[0];//membuat variabel bestKromosom untuk menyimpan kromosom yang memiliki nilai fitness terbaik yang pertamanya di set dengan kromosom index ke 0 dari populasi
        for(int i = 0; i < population.length;i++){//looping untuk mencari kromosom yang  memiliki nilai fitness terbaik
            double fitness = population[i].getFitness();//mengambil nilai fitness dari kromosom ke i
            if(fitness > best){//membandingkan nilai fitness sekarang apakah lebih besar dari nilai fitness terbaik atau tidak (untuk fitness function newFitnessFunction)
                best = fitness;//mengisi nilai best fitness dengan nilai fitness kromosom ke i karena nilai fitness tersebut adalah yang terbaik sampai sekarang
                bestKromosom = population[i];//mengisi best kromosom dengan kromosom ke i karena nilai fitness kromosom i adalah  yang terbaik sampai sekarang
            }
            
//            if(fitness < best){//membandingkan nilai fitness sekarang apakah lebih kecil dari nilai fitness terbaik atau tidak (untuk fitness function fitnessFunction)
//                best = fitness;//mengisi nilai best fitness dengan nilai fitness kromosom ke i karena nilai fitness tersebut adalah yang terbaik sampai sekarang
//                bestKromosom = population[i];//mengisi best kromosom dengan kromosom ke i karena nilai fitness kromosom i adalah  yang terbaik sampai sekarang
//            }
        }
       
        return bestKromosom;//mengembalikan kromosom terbaik pada populasi parameter
    }
    
    
    public LinkedHashMap<Kromosom, Double> set_probabilities_of_population(Kromosom[] population){//method untuk mengurutkan populasi bedasarkan fitnes yang telah di normalisasi
        
        Double totalFitness = 0.0;//membuat variabel total fitnes untuk menyimpan total keseluruhan fitnes dari semua kromosom pada populasi dari parameter
        for(int i = 0; i < population.length;i++){//looping untuk menjumlahkan nilai fitnes setiap kromosom
            totalFitness += population[i].getFitness();//menambahkan total fitness dengan niali fitness kromosom ke i
        }
        LinkedHashMap<Kromosom, Double> sortedPopulation = this.sortingKromosom(population);//megurutkan kromosom bedasarkan nilai fitness dengan memanggil method sorting kromosom
        LinkedHashMap<Kromosom, Double> sortedPopulationNormalization = new LinkedHashMap<>();//membuat linked hash map untuk menyimpan kromosom yang terurut bedasarkan nilai fitness yang telah di normalisasi
        for(Map.Entry<Kromosom, Double> et : sortedPopulation.entrySet()){//melakukan looping untuk menambahkan kromosom yang fitness nya sudah di normalisasi 
            sortedPopulationNormalization.put(et.getKey(), et.getValue()/totalFitness);//memindahkan isi (kromosom dan fitness) dari LinkedHashMap sortedPopulation ke sortedPopulationNormalization yang dimana fitnessnya sudah di normalisasi
        }

        return sortedPopulationNormalization;//mengembalikan populasi yang telah diurutkan bedasarkan fitnes yang telah di normalisasi
        
    }
    
    public Kromosom spinRouletteWheel(RouletteWheel wheel){//method untuk memutar wheel dari roulette wheel yang dimana parameternya adalah wheel nya
        double spin = rd.nextDouble();//melakukan spin wheel dengan menggunakan atribut random
        Kromosom kromosom = wheel.getKromosom(spin);//mencari kromosom / slice dengan panah yang ditunjuk wheel adalah dari variabel spin
        return kromosom;//mengembalikan kromosom dari yang terpilih (ditunjuk oleh panah dari wheel)
    }
    
    public RouletteWheel createRouletteWheel(LinkedHashMap<Kromosom, Double> sortedPopulationNormalization, RouletteWheel wheel){//membuat slice dari wheel miliki roulette wheel dengan parameter populasi yang sudah terurut bedasarkan fitness yang sudah di normalisasi dan wheel nya
        Double total = 0.0;//membuat variabel total untuk menyimpan total nilai fitness sekarang yang dimana awalnya di set 0 karena belum menambahkan slice ke wheel
        int i = 0;//variabel iterator untuk menandakan sekarang sedang ingin menambahkan slice ke berapa
        for(Map.Entry<Kromosom, Double> et : sortedPopulationNormalization.entrySet()){//looping untuk menambahkan slice ke wheel
            wheel.addSlice(i,et.getKey(), total, total+et.getValue());//menambahkan slice ke wheel dengan parameter sekarang ada di slice ke i dan kromosomnya dan total nilai fitness sekarang (sebagai batas bawah slice i) dan total nilai fitness + nilai fitness dari kromosom ke i (sebagai batas atas slice i)
            total += et.getValue();//menambahkan total dengan fitness dari kromosom ke i (untuk dijadikan batas bawah slice berikutnya)
            i++;//menambahkan 1 ke iterator karena sudah menambahkan slice 1 kali ke wheel
        }
        return wheel;//mengembalikan wheel yang telah dibuat potongan slicenya
    }
    
    public Kromosom[] runRouletteWheel(int elitismRate, Kromosom[] populasi){//method untuk menjalankan rouletee wheel dengan parameter elitism rate dan populasi
        int jumlahParent = (int)(populasi.length*(1.0-(elitismRate/100.0)));//menghitung jumlah parent yaitu sebanyak sisa dari elitism
        Kromosom[] parent = new Kromosom[jumlahParent];//membuat array of Kromosom untuk menyimpan parent yang terpilih menggunakan rouletee wheel
        LinkedHashMap<Kromosom, Double> sortedPopulationNormalization = set_probabilities_of_population(populasi);//mengurutkan kromosom bedasarkan fitness yang telah di normalisasi
        RouletteWheel wheel = new RouletteWheel(populasi.length);//membuat object kelas RouletteWheel dengan parameter yaitu banyak parent yang akan dipilih
        wheel = createRouletteWheel(sortedPopulationNormalization,wheel);//membuat slice pada wheel 
        for(int i = 0; i < jumlahParent;i++){//looping untuk memilih parent dengan menggunakan roulette wheel
            parent[i] = spinRouletteWheel(wheel);//memilih parent dengan menggunakan roulette wheel
        }
        return parent;//mengembalikan parent yang terpilih menggunakan roulette wheel
    }
    
    
    
    class RouletteWheel{//kelas RouleteWheel untuk menyimpan wheel
        Kromosom[] population;//atribut populasi dengan tipe data array of Kromosom yaitu untuk menyimpan populasi
        ArrayList<Double> batasBawah;//atribut untuk menyimpan batas bawah dari setiap slice (kromosom) dengan tipe data array list of Double
        ArrayList<Double> batasAtas;//atribut untuk menyimpan batas atas dari setiap slice (kromosom) dengan tipe data array list of Double
        
        public RouletteWheel(int sizePopulation){//constructor kelas RouleteWheel dengan parameter nya adalah ukuran populasi (banyak kromosom pada populasi)
            population = new Kromosom[sizePopulation];//menginisialisasikan atribut population dengan array of Kromosom dengan ukuran populasi (banyak kromosom)
            batasBawah = new ArrayList<>();//menginisialisasikan atribut batas bawah dengan array list of Double
            batasAtas = new ArrayList<>();//menginisialisasikan atribut batas atas dengan array list of Double
        }
        
        public void addSlice(int idx, Kromosom kromosom,Double batasBawah, Double batasAtas){//method untuk menambahkan slice di wheel dengan parameter indexnya (untuk di tempatkan di index), kromosom (untuk melihat di slice tersebut kromosom apa) dan batas atas serta batas bawahnya 
            population[idx] = kromosom;//mengisi populasi index ke idx dengan kromosomnya (untuk menandakan di slice ke idx isinya kromosom yang dari parameter)
            this.batasBawah.add(idx, batasBawah);//mengisi array batas bawah dengan batas bawah dari slice ke idx
            this.batasAtas.add(idx, batasAtas);//mengisi array batas atas dengan batas atas dari slice ke idx
            
        }
        
        public Kromosom getKromosom(double value){//method untuk mendapatkan kromosom pada bagian panah di wheel menunjuk
            Kromosom kromosom = this.population[0];//membuat variabel kromosom intuk menyimpan kromosom yang berada di slice di panah wheel yang pertama kali di set dengan kromosom pada index ke 0 dari populasi
            for(int i = 0; i < batasBawah.size();i++){//looping untuk mencari kromosom yang berada di slice di panah wheel
                if((batasBawah.get(i) < value) && (value <= batasAtas.get(i))){//jika panah dari wheel menunjuk slice dengan nilai nya lebih besar dari batas bawah slice ke i dan lebih kecil sama dengan batas atas slice ke i
                    kromosom = population[i];//mengisi kromosom dengan slice ke i
                    break;//keluar dari looping karen sudah ketemu slice yang ditunjuk oleh panah dari wheel
                }
            }
            return kromosom;//mengembalikan kromosom yang ditunjuk oleh panah dari wheel
        }
    }
    
    public Kromosom spinRankSelection(RankSelection wheel){//method untuk memutar wheel dari rank selection yang dimana parameternya adalah wheel nya
        double spin = rd.nextDouble();//melakukan spin wheel dengan menggunakan atribut random
        Kromosom kromosom = wheel.getKromosom(spin);//mencari kromosom / slice dengan panah yang ditunjuk wheel adalah dari variabel spin
        return kromosom;//mengembalikan kromosom dari yang terpilih (ditunjuk oleh panah dari wheel)
    }
    
    public RankSelection createRankSelection(LinkedHashMap<Kromosom, Double> sortedPopulationNormalization, RankSelection wheel){//membuat slice dari wheel miliki rank selection  dengan parameternya adalah  populasi yang sudah terurut bedasarkan fitness yang sudah di normalisasi dan wheel nya
        Double total = 1.0;//variabel total untuk menyimpan batas atas dari suatu slice yang dimana awalnya di set 1 karena  belum menambahkan slice ke wheel
        int banyakSlice = sortedPopulationNormalization.size();//variabel untuk menyimpan berapa banyak slice yang akan dibuat
        int i = 0;//variabel iterator untuk menandakan sekarang sedang ingin menambahkan slice ke berapa
        for(Map.Entry<Kromosom, Double> et : sortedPopulationNormalization.entrySet()){//looping untuk menambahkan slice ke wheel    
            wheel.addSlice(i,et.getKey(), total - ((banyakSlice-i)/banyakSlice) , total);//menambahkan slice ke wheel dengan parameter sekarang ada di slice ke i dan kromosomnya dan dengan mengurangi total probabilitas yang masih ada dengan banyak slice dikurangi dengan banyaknya kromosom yang sudah ada pada wheel dan dibagi dengan banyaknya slice yang diperlukan dan probabilitas yang sisa
            total = total - ((banyakSlice-i)/banyakSlice);//mengurangi total probabilitas yang masih ada dengan banyak slice dikurangi dengan banyaknya kromosom yang sudah ada pada wheel dan dibagi dengan banyaknya slice yang diperlukan
            i++;//menambahkan 1 ke iterator karena sudah menambahkan slice 1 kali ke wheel
        }
        return wheel;//mengembalikan wheel yang telah dibuat potongan slicenya
    }
    
    public Kromosom[] runRankSelection(int elitismRate, Kromosom[] populasi){//method untuk menjalankan rank selection dengan parameter elitism rate dan populasi
        int jumlahParent = (int)(populasi.length*(1.0-(elitismRate/100.0)));//menghitung jumlah parent yaitu sebanyak sisa dari elitism
        Kromosom[] parent = new Kromosom[jumlahParent];//membuat array of Kromosom untuk menyimpan parent yang terpilih menggunakan rouletee wheel
        LinkedHashMap<Kromosom, Double> sortedPopulationNormalization = set_probabilities_of_population(populasi);//mengurutkan kromosom bedasarkan fitness yang telah di normalisasi
        RankSelection wheel = new RankSelection(populasi.length);//membuat object kelas RankSelection dengan parameter yaitu banyak parent yang akan dipilih
        wheel = createRankSelection(sortedPopulationNormalization,wheel);//membuat slice pada wheel 
        for(int i = 0; i < jumlahParent;i++){//looping untuk memilih parent dengan menggunakan rank selection
            parent[i] = spinRankSelection(wheel);//memilih parent dengan menggunakan rank selection
        }
        return parent;//mengembalikan parent yang terpilih menggunakan roulette wheel
    }
    
    
    class RankSelection{//kelas RankSelection untuk menyimpan wheel dari rank selection
        Kromosom[] population;//atribut populasi dengan tipe data array of Kromosom yaitu untuk menyimpan populasi
        ArrayList<Double> batasBawah;//atribut untuk menyimpan batas bawah dari setiap slice (kromosom) dengan tipe data array list of Double
        ArrayList<Double> batasAtas;//atribut untuk menyimpan batas atas dari setiap slice (kromosom) dengan tipe data array list of Double
        
        public RankSelection(int sizePopulation){//constructor kelas RankSelection dengan parameter nya adalah ukuran populasi (banyak kromosom pada populasi)
            population = new Kromosom[sizePopulation];//menginisialisasikan atribut population dengan array of Kromosom dengan ukuran populasi (banyak kromosom)
            batasBawah = new ArrayList<>();//menginisialisasikan atribut batas bawah dengan array list of Double
            batasAtas = new ArrayList<>();//menginisialisasikan atribut batas atas dengan array list of Double
        }
        
        public void addSlice(int idx, Kromosom kromosom,Double batasBawah, Double batasAtas){//method untuk menambahkan slice di wheel dengan parameter indexnya (untuk di tempatkan di index), kromosom (untuk melihat di slice tersebut kromosom apa) dan batas atas serta batas bawahnya 
            population[idx] = kromosom;//mengisi populasi index ke idx dengan kromosomnya (untuk menandakan di slice ke idx isinya kromosom yang dari parameter)
            this.batasBawah.add(idx, batasBawah);//mengisi array batas bawah dengan batas bawah dari slice ke idx
            this.batasAtas.add(idx, batasAtas);//mengisi array batas atas dengan batas atas dari slice ke idx
            
        }
        
        public Kromosom getKromosom(double value){//method untuk mendapatkan kromosom pada bagian panah di wheel menunjuk
            Kromosom kromosom = this.population[0];//membuat variabel kromosom intuk menyimpan kromosom yang berada di slice di panah wheel yang pertama kali di set dengan kromosom pada index ke 0 dari populasi
            for(int i = 0; i < batasBawah.size();i++){//looping untuk mencari kromosom yang berada di slice di panah wheel
                if((batasBawah.get(i) < value) && (value <= batasAtas.get(i))){//jika panah dari wheel menunjuk slice dengan nilai nya lebih besar dari batas bawah slice ke i dan lebih kecil sama dengan batas atas slice ke i
                    kromosom = population[i];//mengisi kromosom dengan slice ke i
                    break;//keluar dari looping karen sudah ketemu slice yang ditunjuk oleh panah dari wheel
                }
            }
            return kromosom;//mengembalikan kromosom yang ditunjuk oleh panah dari wheel
        }
    }
    
    
    public Kromosom[] runTournamentSelection(int elitismRate, Kromosom[] populasi){//method untuk menjalankan tournament selection dengan parameter elitism rate dan populasi
        int jumlahParent = (int)(populasi.length*(1.0-(elitismRate/100.0)));//menghitung jumlah parent yaitu sebanyak sisa dari elitism
        Kromosom[] parent = new Kromosom[jumlahParent];//menginisialisasi array of kromosom dengan ukuran yaitu jumlah parent yang akan diambil sebagai hasil
        
        for(int j = 0;j<jumlahParent;j++){//looping untuk memasukan data ke dalam array of parent
            for(int i = populasi.length-1;i>0;i--){//looping untuk mengacak - acak isi dari array populasi
                int index = this.rd.nextInt(i+1);//mengambil index secara random dari array populasi
                Kromosom temp = populasi[index];//mengambil populasi ke index untuk ditukar posisinya
                populasi[index] = populasi[i];//menukar array populasi ke index dengan populasi ke i
                populasi[i] = temp;//menukar array populasi ke i dengan populasi ke index
            }
            int idxMax = -1;//variabel untuk menyimpan index dari kromosom yang memiliki index terbesar
            double bestFitness = Integer.MIN_VALUE;//variabel untuk menyimpan fitness dari kromosom yang terbesar (untuk fitness function newFitnessFunction)
            //double bestFitness = Integer.MAX_VALUE;//variabel untuk menyimpan fitness dari kromosom yang terkecil (untuk fitness function fitnessFunction)
            for(int k = 0;k<4;k++){//looping untuk mengambil 4 data teratas dari array
                double currentFitness = populasi[k].getFitness();//menyimpan fitness dari kromosom sementara
                if( currentFitness > bestFitness){//jika fitness yang sekarang lebih besar dari fitness yang pernah dicatat sebelumnya (untuk fitness function newFitnessFunction)
                    bestFitness = currentFitness;//menukar variabel bestFitness dengan fitness yang sekarang terbesar
                    idxMax = k;//index dari fitness yang terbesar diambil dan disimpan
                }
                
//                if( currentFitness < bestFitness){//jika fitness yang sekarang lebih kecil dari fitness yang pernah dicatat sebelumnya (untuk fitness function fitnessFunction)
//                    bestFitness = currentFitness;//menukar variabel bestFitness dengan fitness yang sekarang terkecil
//                    idxMax = k;//index dari fitness yang terbesar diambil dan disimpan
//                }
            }
            parent[j] = populasi[idxMax];//memasukan kormosom yang telah dilakukan selection sebelumnya ke dalam array parent
        }
        return parent;//mengembalikan array paarent sebagai hasil dari tournament selction
    }
    
    public long getTime(){//method untuk mengambil time yang dibutuhkan untuk menjalankan suatu proses
        return this.endTime-this.startTime;//mengembalikan selisih dari waktu selesai dengan waktu mulai
    }
    
    public double getFitness(){//method untuk mengambil fitness yang terbaik yang pernah tercatat
        return this.bestFitness;//mengembalikan jumlah fitness yang terbaik
    }
    
    public double newFitnessFunction(Integer[] kromosom){//method untuk menghitung fitness dari kromosom (yang dimana komentar yang menunjukan baris atau kolom dilihat dimulai dari 1 bukan 0)
        
        int jmlhKotakYangBerisiAngka = 0;//variabel untuk menyimpan ada berapa kotak yang berisi angka dari minesweeper
        double persentaseKotakYangBenar = 0.0;//variabel untuk menyimpan persetanse jumlah kotak yang benar
        for(int i = 0; i < this.ukMineSweeper;i++){//looping baris
            for(int j = 0; j < this.ukMineSweeper;j++){//looping kolom
                int isi = this.mineSweeper[i][j];//mengambil isi dari baris ke i, kolom ke j
                if(isi != -1){//jika baris ke i, kolom ke j ada isinya / angka
                    jmlhKotakYangBerisiAngka++;
                    if(i == 0){//ada di baris paling atas
                        if(j == 0){//ada di kolom paling kiri
                            if(isi == 0){//jika isi kotak (baris ke i, kolom ke j) adalah 0
                                int jmlhPutihSeharusnya = 4;//variabel untuk menyimpan umlah kotak putih seharusnya ada 4
                                int jmlhPutihYangAda = 0;//variabel untuk menyimpan jumlah kotak putih yang ada dari kromosom yang dimana pertamanya di set 0 karena belum dilakukan pengecekkan
                                if(kromosom[0] == 1){//cek baris paling atas, kolom paling kiri putih atau bukan
                                   jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+1 dan kolom ke j+1 pada kromosom adalah putih (1)
                                }
                                if(kromosom[1] == 1){//cek baris paling atas, kolom kedua dari kiri putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+1 dan kolom ke j+2 pada kromosom adalah putih (1)
                                }
                                if(kromosom[(this.ukMineSweeper*1)] == 1){//cek baris kedua kolom paling kiri putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+2 dan kolom ke j+1 pada kromosom adalah putih (1)
                                }
                                if(kromosom[(this.ukMineSweeper*1) + 1] == 1){//cek baris kedua kolom kedua dari kiri putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+2 dan kolom ke j+2 pada kromosom adalah putih (1)
                                }
                                persentaseKotakYangBenar += jmlhPutihYangAda / (jmlhPutihSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                                
                            }
                            else{//jika isi kotak (baris ke i, kolom ke j) adalah bukan 0
                                int jmlhHitamSeharusnya = isi;//jumlah kotak hitam seharusnya adalah sesuai isi kotak (baris ke i, kolom ke j)
                                int jmlhHitamYangAda = 0;//variabel untuk menyimpan jumlah kotak hitam yang ada dari kromosom yang dimana pertamanya di set 0 karena belum dilakukan pengecekkan
                                if(kromosom[0] == 0){//cek baris paling atas, kolom paling kiri hitam atau bukan
                                   jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+1 dan kolom ke j+1 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[1] == 0){//cek baris paling atas, kolom kedua dari kiri hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+1 dan kolom ke j+2 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[(this.ukMineSweeper*1)] == 0){//cek baris kedua kolom paling kiri hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+2 dan kolom ke j+1 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[(this.ukMineSweeper*1) + 1] == 0){//cek baris kedua kolom kedua dari kiri hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+2 dan kolom ke j+2 pada kromosom adalah hitam (0)
                                }
                                
                                if(jmlhHitamYangAda > jmlhHitamSeharusnya){//jika jumlah kotak hitam yang ada lebih dari yang seharusnya
                                    int jmlhKotakHitamBenar = jmlhHitamSeharusnya - (jmlhHitamYangAda - jmlhHitamSeharusnya);//jika jumlah kotak hitam yang ada lebih dari yang seharusnya, dihitung selisih dari jumlah hitam yang ada dikurang jumlah hitam seharusnya agar bisa menghitung seberapa jauh dari jumlah hitam yang seharusnya
                                    if(jmlhKotakHitamBenar < 0){//jika jumlah kotak hitam (yang sudah dihitung kebenarannya) kurang dari 0
                                        jmlhKotakHitamBenar = 0;//mengisi jumlah kotak hitam yang benar dengan 0
                                    }
                                    persentaseKotakYangBenar += jmlhKotakHitamBenar / (jmlhHitamSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                                }
                                else{//jika jumlah kotak hitam yang ada kurang dari sama dengan dari yang seharusnya
                                    persentaseKotakYangBenar += jmlhHitamYangAda / (jmlhHitamSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                                }
                            }
                                
                            
                        }
                        else if (j == (this.ukMineSweeper-1)){//ada di kolom paling kanan
                            if(isi == 0){//jika isi kotak (baris ke i, kolom ke j) adalah 0
                                int jmlhPutihSeharusnya = 4;//variabel untuk menyimpan umlah kotak putih seharusnya ada 4
                                int jmlhPutihYangAda = 0;//variabel untuk menyimpan jumlah kotak putih yang ada dari kromosom yang dimana pertamanya di set 0 karena belum dilakukan pengecekkan
                                
                                if(kromosom[this.ukMineSweeper-2] == 1){//cek baris paling atas, kolom kedua dari kanan putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+1 dan kolom ke j+1 pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper-1] == 1){//cek baris paling atas, kolom paling kanan putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+1 dan kolom ke j pada kromosom adalah putih (1)
                                }
                            
                                if(kromosom[(2*this.ukMineSweeper)-2] == 1){//cek baris kedua, kolom kedua dari kanan putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+2 dan kolom ke j+1 pada kromosom adalah putih (1)
                                }
                                if(kromosom[(2*this.ukMineSweeper)-1] == 1){//cek baris kedua, kolom paling kanan putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+2 dan kolom ke j pada kromosom adalah putih (1)
                                }
                                
                                persentaseKotakYangBenar += jmlhPutihYangAda / (jmlhPutihSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                            }
                            else{//jika isi kotak (baris ke i, kolom ke j) adalah bukan 0
                                int jmlhHitamSeharusnya = isi;//jumlah kotak hitam seharusnya adalah sesuai isi kotak (baris ke i, kolom ke j)
                                int jmlhHitamYangAda = 0;//variabel untuk menyimpan jumlah kotak hitam yang ada dari kromosom yang dimana pertamanya di set 0 karena belum dilakukan pengecekkan
                                
                                if(kromosom[this.ukMineSweeper-2] == 0){//cek baris paling atas, kolom kedua dari kanan hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+1 dan kolom ke j+1 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper-1] == 0){//cek baris paling atas, kolom paling kanan hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+1 dan kolom ke j pada kromosom adalah hitam (0)
                                }
                            
                                if(kromosom[(2*this.ukMineSweeper)-2] == 0){//cek baris kedua, kolom kedua dari kanan hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+2 dan kolom ke j+1 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[(2*this.ukMineSweeper)-1] == 0){//cek baris kedua, kolom paling kanan hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+2 dan kolom ke j pada kromosom adalah hitam (0)
                                }
                                
                                
                                if(jmlhHitamYangAda > jmlhHitamSeharusnya){//jika jumlah kotak hitam yang ada lebih dari yang seharusnya
                                    int jmlhKotakHitamBenar = jmlhHitamSeharusnya - (jmlhHitamYangAda - jmlhHitamSeharusnya);//jika jumlah kotak hitam yang ada lebih dari yang seharusnya, dihitung selisih dari jumlah hitam yang ada dikurang jumlah hitam seharusnya agar bisa menghitung seberapa jauh dari jumlah hitam yang seharusnya
                                    if(jmlhKotakHitamBenar < 0){//jika jumlah kotak hitam (yang sudah dihitung kebenarannya) kurang dari 0
                                        jmlhKotakHitamBenar = 0;//mengisi jumlah kotak hitam yang benar dengan 0
                                    }
                                    persentaseKotakYangBenar += jmlhKotakHitamBenar / (jmlhHitamSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                                }
                                else{//jika jumlah kotak hitam yang ada kurang dari sama dengan dari yang seharusnya
                                    persentaseKotakYangBenar += jmlhHitamYangAda / (jmlhHitamSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                                }
                                
                            }
                            
                            
                            
                            
                            
                        }
                        else{//ada di kolom tengah
                            if(isi == 0){//jika isi kotak (baris ke i, kolom ke j) adalah 0
                                int jmlhPutihSeharusnya = 6;//variabel untuk menyimpan umlah kotak putih seharusnya ada 6
                                int jmlhPutihYangAda = 0;//variabel untuk menyimpan jumlah kotak putih yang ada dari kromosom yang dimana pertamanya di set 0 karena belum dilakukan pengecekkan
                                
                                if(kromosom[j-1] == 1){//cek baris paling atas, kolom ke j putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+1 dan kolom ke j pada kromosom adalah putih (1)
                                }
                                if(kromosom[j] == 1){//cek baris paling atas, kolom ke j+1 putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+1 dan kolom ke j+1 pada kromosom adalah putih (1)
                                }
                                if(kromosom[j+1] == 1){//cek baris paling atas, kolom ke j+2 putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+1 dan kolom ke j+2 pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper+j-1] == 1){//cek baris kedua dari atas, kolom ke j putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+2 dan kolom ke j pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper+j] == 1){//cek baris kedua dari atas, kolom ke j+1 putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+2 dan kolom ke j+1 pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper+j+1] == 1){//cek baris kedua dari atas, kolom ke j+2 putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+2 dan kolom ke j+2 pada kromosom adalah putih (1)
                                }
                                
                                persentaseKotakYangBenar += jmlhPutihYangAda / (jmlhPutihSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                            }
                            else{//jika isi kotak (baris ke i, kolom ke j) adalah bukan 0
                                int jmlhHitamSeharusnya = isi;//jumlah kotak hitam seharusnya adalah sesuai isi kotak (baris ke i, kolom ke j)
                                int jmlhHitamYangAda = 0;//variabel untuk menyimpan jumlah kotak hitam yang ada dari kromosom yang dimana pertamanya di set 0 karena belum dilakukan pengecekkan
                                
                                if(kromosom[j-1] == 0){//cek baris paling atas, kolom ke j hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+1 dan kolom ke j pada kromosom adalah hitam (0)
                                }
                                if(kromosom[j] == 0){//cek baris paling atas, kolom ke j+1 hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+1 dan kolom ke j+1 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[j+1] == 0){//cek baris paling atas, kolom ke j+2 hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+1 dan kolom ke j+2 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper+j-1] == 0){//cek baris kedua dari atas, kolom ke j hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+2 dan kolom ke j pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper+j] == 0){//cek baris kedua dari atas, kolom ke j+1 hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+2 dan kolom ke j+1 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper+j+1] == 0){//cek baris kedua dari atas, kolom ke j+2 hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+2 dan kolom ke j+2 pada kromosom adalah hitam (0)
                                }
                                
                                if(jmlhHitamYangAda > jmlhHitamSeharusnya){//jika jumlah kotak hitam yang ada lebih dari yang seharusnya
                                    int jmlhKotakHitamBenar = jmlhHitamSeharusnya - (jmlhHitamYangAda - jmlhHitamSeharusnya);//jika jumlah kotak hitam yang ada lebih dari yang seharusnya, dihitung selisih dari jumlah hitam yang ada dikurang jumlah hitam seharusnya agar bisa menghitung seberapa jauh dari jumlah hitam yang seharusnya
                                    if(jmlhKotakHitamBenar < 0){//jika jumlah kotak hitam (yang sudah dihitung kebenarannya) kurang dari 0
                                        jmlhKotakHitamBenar = 0;//mengisi jumlah kotak hitam yang benar dengan 0
                                    }
                                    persentaseKotakYangBenar += jmlhKotakHitamBenar / (jmlhHitamSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                                }
                                else{//jika jumlah kotak hitam yang ada kurang dari sama dengan dari yang seharusnya
                                    persentaseKotakYangBenar += jmlhHitamYangAda / (jmlhHitamSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                                }
                                
                            }
                            
                            
                            
                            
                            
                            
                        }
                    }
                    else if(i == (this.ukMineSweeper-1)){//ada di baris paling bawah
                        if(j == 0){//ada di kolom paling kiri
                            if(isi == 0){//jika isi kotak (baris ke i, kolom ke j) adalah 0
                                int jmlhPutihSeharusnya = 4;//variabel untuk menyimpan umlah kotak putih seharusnya ada 4
                                int jmlhPutihYangAda = 0;//variabel untuk menyimpan jumlah kotak putih yang ada dari kromosom yang dimana pertamanya di set 0 karena belum dilakukan pengecekkan
                                if(kromosom[this.ukMineSweeper*(i-1)] == 1){//cek baris kedua dari bawah, kolom paling kiri putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i dan kolom ke j+1 pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*(i-1) + 1] == 1){//cek baris kedua dari bawah, kolom kedua dari kiri putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i dan kolom ke j+2 pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*i] == 1){//cek baris paling bawah, kolom paling kiri putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+1 dan kolom ke j+1 pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*1 + 1] == 1){//cek baris paling bawah, kolom kedua dari kiri putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+1 dan kolom ke j+2 pada kromosom adalah putih (1)
                                }
                                
                                persentaseKotakYangBenar += jmlhPutihYangAda / (jmlhPutihSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                            }
                            else{//jika isi kotak (baris ke i, kolom ke j) adalah bukan 0
                                int jmlhHitamSeharusnya = isi;//jumlah kotak hitam seharusnya adalah sesuai isi kotak (baris ke i, kolom ke j)
                                int jmlhHitamYangAda = 0;//variabel untuk menyimpan jumlah kotak hitam yang ada dari kromosom yang dimana pertamanya di set 0 karena belum dilakukan pengecekkan
                                if(kromosom[this.ukMineSweeper*(i-1)] == 0){//cek baris kedua dari bawah, kolom paling kiri hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i dan kolom ke j+1 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*(i-1) + 1] == 0){//cek baris kedua dari bawah, kolom kedua dari kiri hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i dan kolom ke j+2 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*i] == 0){//cek baris paling bawah, kolom paling kiri hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+1 dan kolom ke j+1 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*1 + 1] == 0){//cek baris paling bawah, kolom kedua dari kiri hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+1 dan kolom ke j+2 pada kromosom adalah hitam (0)
                                }
                                
                                if(jmlhHitamYangAda > jmlhHitamSeharusnya){//jika jumlah kotak hitam yang ada lebih dari yang seharusnya
                                    int jmlhKotakHitamBenar = jmlhHitamSeharusnya - (jmlhHitamYangAda - jmlhHitamSeharusnya);//jika jumlah kotak hitam yang ada lebih dari yang seharusnya, dihitung selisih dari jumlah hitam yang ada dikurang jumlah hitam seharusnya agar bisa menghitung seberapa jauh dari jumlah hitam yang seharusnya
                                    if(jmlhKotakHitamBenar < 0){//jika jumlah kotak hitam (yang sudah dihitung kebenarannya) kurang dari 0
                                        jmlhKotakHitamBenar = 0;//mengisi jumlah kotak hitam yang benar dengan 0
                                    }
                                    persentaseKotakYangBenar += jmlhKotakHitamBenar / (jmlhHitamSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                                }
                                else{//jika jumlah kotak hitam yang ada kurang dari sama dengan dari yang seharusnya
                                    persentaseKotakYangBenar += jmlhHitamYangAda / (jmlhHitamSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                                }
                            }
                            
                             
                             
                             
                             
                        }
                        else if (j == (this.ukMineSweeper-1)){//ada di kolom paling kanan
                             if(isi == 0){//jika isi kotak (baris ke i, kolom ke j) adalah 0
                                int jmlhPutihSeharusnya = 4;//variabel untuk menyimpan umlah kotak putih seharusnya ada 4
                                int jmlhPutihYangAda = 0;//variabel untuk menyimpan jumlah kotak putih yang ada dari kromosom yang dimana pertamanya di set 0 karena belum dilakukan pengecekkan
                                 
                                if(kromosom[this.ukMineSweeper*(i-1) + j - 1] == 1){//cek baris kedua dari bawah, kolom kedua dari kanan putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i dan kolom ke j pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*(i-1) + j] == 1){//cek baris kedua dari bawah, kolom paling kanan putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i dan kolom ke j+1 pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*i + j - 1] == 1){//cek baris paling bawah, kolom kedua dari kanan putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+1 dan kolom ke j pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*i + j] == 1){//cek baris paling bawah, kolom paling kanan putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+1 dan kolom ke j+1 pada kromosom adalah putih (1)
                                }
                                
                                persentaseKotakYangBenar += jmlhPutihYangAda / (jmlhPutihSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                             }
                             else{//jika isi kotak (baris ke i, kolom ke j) adalah bukan 0
                                int jmlhHitamSeharusnya = isi;//jumlah kotak hitam seharusnya adalah sesuai isi kotak (baris ke i, kolom ke j)
                                int jmlhHitamYangAda = 0;//variabel untuk menyimpan jumlah kotak hitam yang ada dari kromosom yang dimana pertamanya di set 0 karena belum dilakukan pengecekkan
                                
                                if(kromosom[this.ukMineSweeper*(i-1) + j - 1] == 0){//cek baris kedua dari bawah, kolom kedua dari kanan hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i dan kolom ke j pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*(i-1) + j] == 0){//cek baris kedua dari bawah, kolom paling kanan hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i dan kolom ke j+1 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*i + j - 1] == 0){//cek baris paling bawah, kolom kedua dari kanan hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+1 dan kolom ke j pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*i + j] == 0){//cek baris paling bawah, kolom paling kanan hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+1 dan kolom ke j+1 pada kromosom adalah hitam (0)
                                }
                                
                                if(jmlhHitamYangAda > jmlhHitamSeharusnya){//jika jumlah kotak hitam yang ada lebih dari yang seharusnya
                                    int jmlhKotakHitamBenar = jmlhHitamSeharusnya - (jmlhHitamYangAda - jmlhHitamSeharusnya);//jika jumlah kotak hitam yang ada lebih dari yang seharusnya, dihitung selisih dari jumlah hitam yang ada dikurang jumlah hitam seharusnya agar bisa menghitung seberapa jauh dari jumlah hitam yang seharusnya
                                    if(jmlhKotakHitamBenar < 0){//jika jumlah kotak hitam (yang sudah dihitung kebenarannya) kurang dari 0
                                        jmlhKotakHitamBenar = 0;//mengisi jumlah kotak hitam yang benar dengan 0
                                    }
                                    persentaseKotakYangBenar += jmlhKotakHitamBenar / (jmlhHitamSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                                }
                                else{//jika jumlah kotak hitam yang ada kurang dari sama dengan dari yang seharusnya
                                    persentaseKotakYangBenar += jmlhHitamYangAda / (jmlhHitamSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                                }
                             }
                            
                             
                             
                             
                        }
                        else{//ada di kolom tengah
                            if(isi == 0){//jika isi kotak (baris ke i, kolom ke j) adalah 0
                                int jmlhPutihSeharusnya = 6;//variabel untuk menyimpan umlah kotak putih seharusnya ada 6
                                int jmlhPutihYangAda = 0;//variabel untuk menyimpan jumlah kotak putih yang ada dari kromosom yang dimana pertamanya di set 0 karena belum dilakukan pengecekkan
                                
                                if(kromosom[this.ukMineSweeper*(i-1) + j -1] == 1){//cek baris kedua dari bawah, kolom ke j putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i dan kolom ke j pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*(i-1) + j] == 1){//cek baris kedua dari bawah, kolom ke j+1 putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i dan kolom ke j+1 pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*(i-1) + j + 1] == 1){//cek baris kedua dari bawah, kolom ke j+2 putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i dan kolom ke j+2 pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*i + j - 1] == 1){//cek baris paling bawah, kolom ke j putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+1 dan kolom ke j pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*i + j] == 1){//cek baris paling bawah, kolom ke j+1 putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+1 dan kolom ke j+1 pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*1 + j + 1] == 1){//cek baris paling bawah, kolom ke j+2 putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+1 dan kolom ke j+2 pada kromosom adalah putih (1)
                                }
                                
                                persentaseKotakYangBenar += jmlhPutihYangAda / (jmlhPutihSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                            }
                            else{//jika isi kotak (baris ke i, kolom ke j) adalah bukan 0
                                int jmlhHitamSeharusnya = isi;//jumlah kotak hitam seharusnya adalah sesuai isi kotak (baris ke i, kolom ke j)
                                int jmlhHitamYangAda = 0;//variabel untuk menyimpan jumlah kotak hitam yang ada dari kromosom yang dimana pertamanya di set 0 karena belum dilakukan pengecekkan
                                
                                if(kromosom[this.ukMineSweeper*(i-1) + j -1] == 0){//cek baris kedua dari bawah, kolom ke j hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i dan kolom ke j pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*(i-1) + j] == 0){//cek baris kedua dari bawah, kolom ke j+1 hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i dan kolom ke j+1 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*(i-1) + j + 1] == 0){//cek baris kedua dari bawah, kolom ke j+2 hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i dan kolom ke j+2 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*i + j - 1] == 0){//cek baris paling bawah, kolom ke j hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+1 dan kolom ke j pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*i + j] == 0){//cek baris paling bawah, kolom ke j+1 hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+1 dan kolom ke j+1 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*1 + j + 1] == 0){//cek baris paling bawah, kolom ke j+2 hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+1 dan kolom ke j+2 pada kromosom adalah hitam (0)
                                }
                                
                                if(jmlhHitamYangAda > jmlhHitamSeharusnya){//jika jumlah kotak hitam yang ada lebih dari yang seharusnya
                                    int jmlhKotakHitamBenar = jmlhHitamSeharusnya - (jmlhHitamYangAda - jmlhHitamSeharusnya);//jika jumlah kotak hitam yang ada lebih dari yang seharusnya, dihitung selisih dari jumlah hitam yang ada dikurang jumlah hitam seharusnya agar bisa menghitung seberapa jauh dari jumlah hitam yang seharusnya
                                    if(jmlhKotakHitamBenar < 0){//jika jumlah kotak hitam (yang sudah dihitung kebenarannya) kurang dari 0
                                        jmlhKotakHitamBenar = 0;//mengisi jumlah kotak hitam yang benar dengan 0
                                    }
                                    persentaseKotakYangBenar += jmlhKotakHitamBenar / (jmlhHitamSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                                }
                                else{//jika jumlah kotak hitam yang ada kurang dari sama dengan dari yang seharusnya
                                    persentaseKotakYangBenar += jmlhHitamYangAda / (jmlhHitamSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                                }
                            }
                            
                            
                            
                            
                        }
                    }
                    else{//ada di baris di tengah
                        if(j == 0){//ada di kolom paling kiri
                            if(isi == 0){//jika isi kotak (baris ke i, kolom ke j) adalah 0
                                int jmlhPutihSeharusnya = 6;//variabel untuk menyimpan umlah kotak putih seharusnya ada 6
                                int jmlhPutihYangAda = 0;//variabel untuk menyimpan jumlah kotak putih yang ada dari kromosom yang dimana pertamanya di set 0 karena belum dilakukan pengecekkan
                                
                                if(kromosom[this.ukMineSweeper*(i-1) + j] == 1){//cek baris i, kolom paling kiri putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i dan kolom ke j+1 pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*(i-1) + j + 1] == 1){//cek baris i, kolom kedua dari kiri putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i dan kolom ke j+2 pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*i + j] == 1){//cek baris ke i+1, kolom paling kiri putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+1 dan kolom ke j+1 pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*i + j + 1] == 1){//cek baris i+1, kolom kedua dari kiri putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+1 dan kolom ke j+2 pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*(i+1) + j] == 1){//cek baris ke i+2, kolom paling kiri putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+2 dan kolom ke j+1 pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*(i+1) + j + 1] == 1){//cek baris i+2, kolom kedua dari kiri putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+2 dan kolom ke j+2 pada kromosom adalah putih (1)
                                }
                                
                                persentaseKotakYangBenar += jmlhPutihYangAda / (jmlhPutihSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                            }
                            else{//jika isi kotak (baris ke i, kolom ke j) adalah bukan 0
                                int jmlhHitamSeharusnya = isi;//jumlah kotak hitam seharusnya adalah sesuai isi kotak (baris ke i, kolom ke j)
                                int jmlhHitamYangAda = 0;//variabel untuk menyimpan jumlah kotak hitam yang ada dari kromosom yang dimana pertamanya di set 0 karena belum dilakukan pengecekkan
                                
                                
                                if(kromosom[this.ukMineSweeper*(i-1) + j] == 0){//cek baris i, kolom paling kiri hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i dan kolom ke j+1 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*(i-1) + j + 1] == 0){//cek baris i, kolom kedua dari kiri hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i dan kolom ke j+2 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*i + j] == 0){//cek baris ke i+1, kolom paling kiri hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+1 dan kolom ke j+1 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*i + j + 1] == 0){//cek baris i+1, kolom kedua dari kiri hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+1 dan kolom ke j+2 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*(i+1) + j] == 0){//cek baris ke i+2, kolom paling kiri hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+2 dan kolom ke j+1 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*(i+1) + j + 1] == 0){//cek baris i+2, kolom kedua dari kiri hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+2 dan kolom ke j+2 pada kromosom adalah hitam (0)
                                }
                                
                                if(jmlhHitamYangAda > jmlhHitamSeharusnya){//jika jumlah kotak hitam yang ada lebih dari yang seharusnya
                                    int jmlhKotakHitamBenar = jmlhHitamSeharusnya - (jmlhHitamYangAda - jmlhHitamSeharusnya);//jika jumlah kotak hitam yang ada lebih dari yang seharusnya, dihitung selisih dari jumlah hitam yang ada dikurang jumlah hitam seharusnya agar bisa menghitung seberapa jauh dari jumlah hitam yang seharusnya
                                    if(jmlhKotakHitamBenar < 0){//jika jumlah kotak hitam (yang sudah dihitung kebenarannya) kurang dari 0
                                        jmlhKotakHitamBenar = 0;//mengisi jumlah kotak hitam yang benar dengan 0
                                    }
                                    persentaseKotakYangBenar += jmlhKotakHitamBenar / (jmlhHitamSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                                }
                                else{//jika jumlah kotak hitam yang ada kurang dari sama dengan dari yang seharusnya
                                    persentaseKotakYangBenar += jmlhHitamYangAda / (jmlhHitamSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                                }
                            }
                            
                            
                            
                                
                        }
                        else if (j == (this.ukMineSweeper-1)){//ada di kolom paling kanan
                            if(isi == 0){//jika isi kotak (baris ke i, kolom ke j) adalah 0
                                int jmlhPutihSeharusnya = 6;//variabel untuk menyimpan umlah kotak putih seharusnya ada 6
                                int jmlhPutihYangAda = 0;//variabel untuk menyimpan jumlah kotak putih yang ada dari kromosom yang dimana pertamanya di set 0 karena belum dilakukan pengecekkan
                                
                                if(kromosom[this.ukMineSweeper*(i-1) + j-1] == 1){//cek baris ke i, kolom kedua dari kanan putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i dan kolom ke j pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*(i-1) + j] == 1){//cek baris ke i, kolom paling kanan putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i dan kolom ke j+1 pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*i + j-1] == 1){//cek baris ke i+1, kolom kedua dari kanan putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+1 dan kolom ke j pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*i + j] == 1){//cek baris ke i+1, kolom paling kanan putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+1 dan kolom ke j+1 pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*(i+1) + j-1] == 1){//cek baris ke i+2, kolom kedua dari kanan putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+2 dan kolom ke j pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*(i+1) + j] == 1){//cek baris ke i+2, kolom paling kanan putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+2 dan kolom ke j+1 pada kromosom adalah putih (1)
                                }
                                
                                persentaseKotakYangBenar += jmlhPutihYangAda / (jmlhPutihSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                                
                            }
                            else{//jika isi kotak (baris ke i, kolom ke j) adalah bukan 0
                                int jmlhHitamSeharusnya = isi;//jumlah kotak hitam seharusnya adalah sesuai isi kotak (baris ke i, kolom ke j)
                                int jmlhHitamYangAda = 0;//variabel untuk menyimpan jumlah kotak hitam yang ada dari kromosom yang dimana pertamanya di set 0 karena belum dilakukan pengecekkan
                                
                                if(kromosom[this.ukMineSweeper*(i-1) + j-1] == 0){//cek baris ke i, kolom kedua dari kanan hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i dan kolom ke j pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*(i-1) + j] == 0){//cek baris ke i, kolom paling kanan hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i dan kolom ke j+1 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*i + j-1] == 0){//cek baris ke i+1, kolom kedua dari kanan hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+1 dan kolom ke j pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*i + j] == 0){//cek baris ke i+1, kolom paling kanan hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+1 dan kolom ke j+1 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*(i+1) + j-1] == 0){//cek baris ke i+2, kolom kedua dari kanan hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+2 dan kolom ke j pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*(i+1) + j] == 0){//cek baris ke i+2, kolom paling kanan hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+2 dan kolom ke j+1 pada kromosom adalah hitam (0)
                                }
                                
                                if(jmlhHitamYangAda > jmlhHitamSeharusnya){//jika jumlah kotak hitam yang ada lebih dari yang seharusnya
                                    int jmlhKotakHitamBenar = jmlhHitamSeharusnya - (jmlhHitamYangAda - jmlhHitamSeharusnya);//jika jumlah kotak hitam yang ada lebih dari yang seharusnya, dihitung selisih dari jumlah hitam yang ada dikurang jumlah hitam seharusnya agar bisa menghitung seberapa jauh dari jumlah hitam yang seharusnya
                                    if(jmlhKotakHitamBenar < 0){//jika jumlah kotak hitam (yang sudah dihitung kebenarannya) kurang dari 0
                                        jmlhKotakHitamBenar = 0;//mengisi jumlah kotak hitam yang benar dengan 0
                                    }
                                    persentaseKotakYangBenar += jmlhKotakHitamBenar / (jmlhHitamSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                                }
                                else{//jika jumlah kotak hitam yang ada kurang dari sama dengan dari yang seharusnya
                                    persentaseKotakYangBenar += jmlhHitamYangAda / (jmlhHitamSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                                }
                                
                            }
                            
                            
                            
                            
                        }
                        else{//ada di kolom tengah
                            if(isi == 0){//jika isi kotak (baris ke i, kolom ke j) adalah 0
                                int jmlhPutihSeharusnya = 9;//variabel untuk menyimpan umlah kotak putih seharusnya ada 9
                                int jmlhPutihYangAda = 0;//variabel untuk menyimpan jumlah kotak putih yang ada dari kromosom yang dimana pertamanya di set 0 karena belum dilakukan pengecekkan
                                
                                if(kromosom[this.ukMineSweeper*(i-1) + j-1] == 1){//cek baris ke i, kolom ke j putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i dan kolom ke j pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*(i-1) + j] == 1){//cek baris ke i, kolom ke j+1 putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i dan kolom ke j+1 pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*(i-1) + j+1] == 1){//cek baris ke i, kolom ke j+2 putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i dan kolom ke j+2 pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*i + j-1] == 1){//cek baris ke i+1, kolom ke j putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+1 dan kolom ke j pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*i + j] == 1){//cek baris ke i+1, kolom ke j+1 putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+1 dan kolom ke j+1 pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*i + j+1] == 1){//cek baris ke i+1, kolom ke j+2 putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+1 dan kolom ke j+2 pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*(i+1) + j-1] == 1){//cek baris ke i+2, kolom ke j putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1+2 karena baris ke i dan kolom ke j pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*(i+1) + j] == 1){//cek baris ke i+2, kolom ke j+1 putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+2 dan kolom ke j+1 pada kromosom adalah putih (1)
                                }
                                if(kromosom[this.ukMineSweeper*(i+1) + j+1] == 1){//cek baris ke i+2, kolom ke j+2 putih atau bukan
                                    jmlhPutihYangAda++;//menambahkan variabel jmlhPutihYangAda dengan 1 karena baris ke i+2 dan kolom ke j+2 pada kromosom adalah putih (1)
                                }
                                
                                persentaseKotakYangBenar += jmlhPutihYangAda / (jmlhPutihSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                                
                            }
                            else{//jika isi kotak (baris ke i, kolom ke j) adalah bukan 0
                                int jmlhHitamSeharusnya = isi;//jumlah kotak hitam seharusnya adalah sesuai isi kotak (baris ke i, kolom ke j)
                                int jmlhHitamYangAda = 0;//variabel untuk menyimpan jumlah kotak hitam yang ada dari kromosom yang dimana pertamanya di set 0 karena belum dilakukan pengecekkan
                                
                                if(kromosom[this.ukMineSweeper*(i-1) + j-1] == 0){//cek baris ke i, kolom ke j hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i dan kolom ke j pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*(i-1) + j] == 0){//cek baris ke i, kolom ke j+1 hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i dan kolom ke j+1 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*(i-1) + j+1] == 0){//cek baris ke i, kolom ke j+2 hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i dan kolom ke j+2 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*i + j-1] == 0){//cek baris ke i+1, kolom ke j hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+1 dan kolom ke j pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*i + j] == 0){//cek baris ke i+1, kolom ke j+1 hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+1 dan kolom ke j+1 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*i + j+1] == 0){//cek baris ke i+1, kolom ke j+2 hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+1 dan kolom ke j+2 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*(i+1) + j-1] == 0){//cek baris ke i+2, kolom ke j hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1+2 karena baris ke i dan kolom ke j pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*(i+1) + j] == 0){//cek baris ke i+2, kolom ke j+1 hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+2 dan kolom ke j+1 pada kromosom adalah hitam (0)
                                }
                                if(kromosom[this.ukMineSweeper*(i+1) + j+1] == 0){//cek baris ke i+2, kolom ke j+2 hitam atau bukan
                                    jmlhHitamYangAda++;//menambahkan variabel jmlhHitamYangAda dengan 1 karena baris ke i+2 dan kolom ke j+2 pada kromosom adalah hitam (0)
                                }
                                
                                if(jmlhHitamYangAda > jmlhHitamSeharusnya){//jika jumlah kotak hitam yang ada lebih dari yang seharusnya
                                    int jmlhKotakHitamBenar = jmlhHitamSeharusnya - (jmlhHitamYangAda - jmlhHitamSeharusnya);//jika jumlah kotak hitam yang ada lebih dari yang seharusnya, dihitung selisih dari jumlah hitam yang ada dikurang jumlah hitam seharusnya agar bisa menghitung seberapa jauh dari jumlah hitam yang seharusnya
                                    if(jmlhKotakHitamBenar < 0){//jika jumlah kotak hitam (yang sudah dihitung kebenarannya) kurang dari 0
                                        jmlhKotakHitamBenar = 0;//mengisi jumlah kotak hitam yang benar dengan 0
                                    }
                                    persentaseKotakYangBenar += jmlhKotakHitamBenar / (jmlhHitamSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                                }
                                else{//jika jumlah kotak hitam yang ada kurang dari sama dengan dari yang seharusnya
                                    persentaseKotakYangBenar += jmlhHitamYangAda / (jmlhHitamSeharusnya*1.0);//menambahkan persentaseKotakYangBenar dengan persentase kebenaran pada kromosom ini (bila jumlah yang ada sama dengan jumlah yang seharusnya maka angka 0 akan mendapat nilai yang penuh / sudah benar)
                                }
                                
                            }
                            
                            
                            
                            
                            
                                
                        }
                    }
                }
            }
        }
        
        double fitnessValue = persentaseKotakYangBenar / (jmlhKotakYangBerisiAngka*1.0) * 100.0;//menghitung nilai fitness dengan menghitung seluruh jumlah nilai dari seluruh angka yang ada per jumlah dari seluruh angka yang ada (ex: bila seluruh angka mendapat nilai full (1) dan ada 10 angka maka perhitungannya menjadi 10/10*100)
        return fitnessValue;//mengembalikan nilai fitness dari kromosom yang ada
    }
    
    
}

