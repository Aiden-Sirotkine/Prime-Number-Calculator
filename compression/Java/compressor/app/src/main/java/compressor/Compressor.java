package compressor;

import java.io.*;
import org.xerial.snappy.*;
import net.jpountz.lz4.*; 

public class Compressor {
    
    public Compressor() {

    }

    public byte[] compressDataLZ4(byte[] data, boolean debug) {
        
      // Compressor Object 
      LZ4Factory factory = LZ4Factory.nativeInstance();

      final int decompressedLength = data.length;

      if (debug) {
        System.out.println("\nDecompressed Length: " + decompressedLength);

      }

      // Start Stopwatch  
      long startTime = System.currentTimeMillis();


      // Compress Data
      LZ4Compressor compressor = factory.highCompressor(17); 
      int maxCompressedLength = compressor.maxCompressedLength(decompressedLength);
      byte[] compressed = new byte[maxCompressedLength];
      int compressedLength = compressor.compress(data, 0, decompressedLength, compressed, 0, maxCompressedLength);

      // Stop Stopwatch 
      long endTime = System.currentTimeMillis();

      if (debug) {
        System.out.println("Compressed Length: " + compressedLength);
        
        long duration = endTime - startTime;

        System.out.println("Compression Time: " + duration + " ms");
      }

      return compressed;

    }


    public byte[] decompressDataLZ4(byte[] data, boolean debug, int originalLength) {
      
      // Compressor Object 
      LZ4Factory factory = LZ4Factory.fastestInstance();

      //final int compressedLength = data.length; 

      if (debug) {
        System.out.println("\nOriginal Length: " + originalLength);

      }

      // Start Stopwatch 
      long startTime = System.currentTimeMillis();

      // Decompress Data 
      //LZ4SafeDecompressor decompressor = factory.safeDecompressor();
      //byte[] restored = new byte[originalLength];
      //int compressedLength2 = decompressor.decompress(data, 0, originalLength, restored, 0);
      LZ4FastDecompressor decompressor = factory.fastDecompressor();
      byte[] restored = new byte[originalLength];
      int compressedLength2 = decompressor.decompress(data, 0, restored, 0, originalLength);

      // Stop Stopwatch 
      long endTime = System.currentTimeMillis();

      if (debug) {
        System.out.println("Decompressed Length: " + restored.length);

        long duration = endTime - startTime;

        System.out.println("Decompression Time: " + duration + " ms");

        //double compressionRatio = (originalLength - restored.length) / originalLength;

       // System.out.println("\nCompression Ratio: " + (compressionRatio * 100) + "%");

      } 

      return restored;

    }

    public void compressFile(String inputName, String outputName) {

        File inputFile = new File(inputName);
        File outputFile = new File(outputName + ".snappy"); 

        // Read input file contents into a byte array
        byte[] input; 
        try (FileInputStream inputStream = new FileInputStream(inputFile)) {
            input = inputStream.readAllBytes();
        }
        catch (IOException e) {
            System.err.println("\nError reading input file: " + e.getMessage()); 
            return; 
        }

        System.out.println("\nInput File Size: " + input.length + " bytes");

        // Compress input data 
        byte[] compressed;
        try {
            compressed = Snappy.compress(input);
        }

        catch (IOException e) {
            System.err.println("\nError compressing data: " + e.getMessage());
            return;
        }

        // Write compressed data to output file
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(compressed);
        }
        catch (IOException e) {
            System.err.println("\nError writing output file: " + e.getMessage());
            return; 
        }

            System.out.println("\nCompressed size: " + compressed.length);
            //System.out.println("Output file: " + outputFile.getAbsolutePath());
    }

    public void decompressFile(String inputName, String outputName) {
        File inputFile = new File(inputName);
        File outputFile = new File(outputName); 

        // Read input file contents into a byte array
        byte[] input; 
        try (FileInputStream inputStream = new FileInputStream(inputFile)) {
            input = inputStream.readAllBytes();
        }
        catch (IOException e) {
            System.err.println("\nError reading input file: " + e.getMessage()); 
            return; 
        }

        // Decompress input data
        byte[] decompressed;
        try {
            decompressed = Snappy.uncompress(input);
        }

        catch (IOException e) {
            System.err.println("\nError decompressing data: " + e.getMessage());
            return;
        }

        // Write decompressed data to output file
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(decompressed);
        }
        catch (IOException e) {
            System.err.println("\nError writing output file: " + e.getMessage());
            return; 
        }

            System.out.println("Output file: " + outputFile.getAbsolutePath());


    }
}
