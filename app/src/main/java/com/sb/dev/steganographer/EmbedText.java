package com.sb.dev.steganographer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;


public class EmbedText
    {
        public static Bitmap embed(Bitmap originalImage1, String text)
            {
                Bitmap originalImage=originalImage1.copy(Bitmap.Config.ARGB_8888,true);
                int x = 0, y = 0, asciiValue;//x= x coordinate, y=y coordinate of image starting from top left .
                final int EXTRACTOR = 0x00000001;//BitMask to extract last bit of character.
                final int ZEROATLAST=0xfffffffe;
                for (int i = 0; i <= text.length(); i++)
                    {
                        if (i < text.length())
                            {
                                asciiValue = text.charAt(i);
                            } else
                            {
                                asciiValue = 0;// Will be used at the end to mark the end of text.
                            }
                        for (int j = 0; j < 8; j++)//8 bits forms a character.
                            {
                                int bitValue = asciiValue & EXTRACTOR;//extracts single bit from the character

                                if (bitValue == 1)
                                    {
                                        originalImage.setPixel(x, y, originalImage.getPixel(x, y) | EXTRACTOR);//Replaces least significant value of the blue color of the pixel with 1.
                                    } else
                                    {
                                        originalImage.setPixel(x, y, originalImage.getPixel(x, y) & ZEROATLAST);//Replaces least significant value of the blue color of the pixel with 0.
                                    }
                                x++;
                                if (x > originalImage.getWidth())
                                    {
                                        x = 0;
                                        y++;
                                    }
                                asciiValue = asciiValue >> 1;
                            }
                    }
                Log.d("EmbedText","process completed");

                return originalImage;
            }
    }
