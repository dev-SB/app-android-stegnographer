package com.sb.dev.steganographer;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DecodeFragment extends Fragment
    {
        final int REQUEST_CODE = 23;
        private Context thisContext;
        private FloatingActionButton decodeFab;
        private Uri uri = null;
        private TextView decodeTextView;
        private String decodedTextMainScreen;
        public static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
        private static final String DECODED_TEXT_FILE_FOLDER = "Decoded Text Files";
        private static final String DECODED_TEXT_FILE_NAME = "Decodedtxt";
        private static String decodedTextFilePath = null;
        private static String encodedImageName = null;
        private ProgressBar mProgressBar;

        public DecodeFragment()
            {

            }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                try
                    {
                        thisContext = getActivity().getApplicationContext();
                    } catch (NullPointerException e)
                    {
                        Log.d("onCreate Decode", e.getMessage() + " context exception");
                    }


            }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
            {
                super.onViewCreated(view, savedInstanceState);
                decodeTextView = view.findViewById(R.id.decode_text);
                mProgressBar=view.findViewById(R.id.progressBarDecode);
            }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
            {
                super.onCreateOptionsMenu(menu, inflater);
                inflater.inflate(R.menu.menu_decode, menu);
            }

        @Override
        public boolean onOptionsItemSelected(MenuItem item)
            {
                switch (item.getItemId())
                    {
                        case R.id.menu_decode_save:
                            Toast.makeText(thisContext, "Save Clicked", Toast.LENGTH_SHORT).show();
                            checkPermission();
                            break;
                        case R.id.menu_decode_copy:
                            Toast.makeText(thisContext, "Text Copied!", Toast.LENGTH_SHORT).show();
                            copyDecodedText();
                            break;
                    }

                return super.onOptionsItemSelected(item);

            }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
            {
                View view = inflater.inflate(R.layout.layout_decode, container, false);

                decodeFab = view.findViewById(R.id.decode_fab);
                decodeFab.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                            {
                                setHasOptionsMenu(true);
                                decodeTextView.setText("");

                                searchImage();
                            }
                    });
                return view;
            }

        private void searchImage()
            {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data)
            {
                super.onActivityResult(requestCode, resultCode, data);
                if (REQUEST_CODE == requestCode && resultCode == Activity.RESULT_OK)
                    {

                        if (data != null)
                            {
                                uri = data.getData();
                                Log.i("image imported", uri.toString());
                                new decodeAsyncTask().execute(uri);
                            } else
                            {
                                Toast.makeText(thisContext, "Please Select an Image", Toast.LENGTH_SHORT).show();
                            }
                    }
            }

        private class decodeAsyncTask extends AsyncTask<Uri, Void, String>
            {
                @Override
                protected String doInBackground(Uri... uris)
                    {
                        Bitmap encodedImage = null;
                        try
                            {
                                encodedImage = MediaStore.Images.Media.getBitmap(thisContext.getContentResolver(), uris[0]);

                                String temp = uris[0].getLastPathSegment();
                                int index = temp.lastIndexOf('/');
                                int endIndex = temp.lastIndexOf('.');
                                encodedImageName = temp.substring(index + 1, endIndex);
                                Log.i("async image uri", encodedImageName);
                            } catch (IOException e)
                            {
                                Log.e("Decode asynctask", e.getMessage());
                            }
                        StringBuilder mStringBuilder = new StringBuilder(ExtractText.extract(encodedImage));
                        String decodedText = mStringBuilder.toString();
                        Log.v("decoded text:", decodedText);
                        return decodedText;
                    }

                @Override
                protected void onPostExecute(String s)
                    {
                        super.onPostExecute(s);
                        mProgressBar.setVisibility(View.GONE);
                        decodedTextMainScreen = s;
                        decodeTextView.setText(s);

                    }

                @Override
                protected void onPreExecute()
                    {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
            }

        private void checkPermission()
            {
                if (ContextCompat.checkSelfPermission(thisContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
                    {
                        Log.d("Permission", "asking for permissions");

                        requestPermissions(new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
                        //requestPermissions should be used with fragment instead of AppCompat.requestPermissions
                        Toast.makeText(thisContext, "Permission asked", Toast.LENGTH_SHORT).show();
                    } else
                    {
                        Log.d("Permission", "Permission is already granted");
                        saveDecodedText();
                    }
            }


        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
            {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                Log.d("Permission", "Request Permission Result");
                if (requestCode == PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE)
                    {
                        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                            {
                                Log.d("Permission", "Permission Granted");
                                Toast.makeText(thisContext, "Permission Granted", Toast.LENGTH_SHORT).show();
                                saveDecodedText();

                            } else
                            {
                                Log.d("Permission", "Permission Denied");
                            }
                    }
            }

        private void saveDecodedText()
            {
                if (isExtStorageWritable())
                    {
                        File dir = getPublicStorageDir();
                        File file = new File(dir, DECODED_TEXT_FILE_NAME.concat("_").concat(encodedImageName).concat(".txt"));
                        decodedTextFilePath = file.toString();
                        Log.d("decode file path", decodedTextFilePath);
                        if (file.exists()) file.delete();
                        try
                            {
                                FileWriter writer = new FileWriter(file);
                                writer.append(decodedTextMainScreen);
                                writer.flush();
                                writer.close();
                                Toast.makeText(thisContext, "File Saved", Toast.LENGTH_SHORT).show();

                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                    } else
                    {
                        Toast.makeText(thisContext, "Storage not available", Toast.LENGTH_SHORT).show();
                    }
            }

        private File getPublicStorageDir()
            {
                File file = new File(Environment.getExternalStorageDirectory(),
                        EncodeFragment.APP_NAME + "/" + DECODED_TEXT_FILE_FOLDER);

                if (file.exists())
                    {
                        file.delete();
                    } else
                    {
                        if (!file.mkdirs())
                            {
                                Log.e("File", "Directory creation Unsuccessful");
                            }
                    }
                return file;
            }

        private boolean isExtStorageWritable()
            {
                String state = Environment.getExternalStorageState();
                return Environment.MEDIA_MOUNTED.equals(state);

            }

        private void copyDecodedText()
            {
                ClipboardManager manager = (ClipboardManager) thisContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("simple text", decodedTextMainScreen);
                manager.setPrimaryClip(clipData);
            }
    }
