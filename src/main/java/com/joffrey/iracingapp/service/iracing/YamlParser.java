package com.joffrey.iracingapp.service.iracing;

import static com.joffrey.iracingapp.model.defines.YamlState.keysep;
import static com.joffrey.iracingapp.model.defines.YamlState.newline;
import static com.joffrey.iracingapp.model.defines.YamlState.space;

import com.joffrey.iracingapp.model.defines.YamlState;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

@Component
public class YamlParser {

    public String parseYaml(String data, String path, int len) {

        if (!data.isEmpty() && path != null && len != 0) {
            // make sure we set this to something
            String val = "";
            len = 0;

            int depth = 0;
            YamlState state = space;

            String keystr = "";
            int keylen = 0;

            String valuestr = "";
            int valuelen = 0;

            StringBuilder pathptr = new StringBuilder(path);
            pathptr.setLength(100);
            int pathdepth = 0;

            for (int i = 0; i < data.length(); i++) {
                data = data.substring(1);
                switch (data.charAt(0)) {
                    case ' ':
                        if (state == newline) {
                            state = space;
                        }
                        if (state == space) {
                            depth++;
                        } else if (state == YamlState.key) {
                            keylen++;
                        } else if (state == YamlState.value) {
                            valuelen++;
                        }
                        break;
                    case '-':
                        if (state == newline) {
                            state = space;
                        }
                        if (state == space) {
                            depth++;
                        } else if (state == YamlState.key) {
                            keylen++;
                        } else if (state == YamlState.value) {
                            valuelen++;
                        } else if (state == keysep) {
                            state = YamlState.value;
                            valuestr = data;
                            valuelen = 1;
                        }
                        break;
                    case ':':
                        if (state == YamlState.key) {
                            state = keysep;
                            keylen++;
                        } else if (state == keysep) {
                            state = YamlState.value;
                            valuestr = data;
                        } else if (state == YamlState.value) {
                            valuelen++;
                        }
                        break;
                    case '\n':
                    case '\r':
                        if (state != newline) {
                            if (depth < pathdepth) {
                                return "";
                            } else if (keylen != 0 && 0 == keystr.substring(0, keylen).compareTo(pathptr.substring(0, keylen))) {
                                // if(keylen && 0 == strncmp(keystr, pathptr, keylen))

                                boolean found = true;
                                //do we need to test the value?
                                if (pathptr.charAt(keylen) == '{') {

                                    //search for closing brace
                                    int pathvaluelen = keylen + 1;
                                    while ((pathptr.charAt(pathvaluelen) != '}')) {
                                        pathvaluelen++;
                                    }

                                    if (valuelen == pathvaluelen - (keylen + 1) && 0 == valuestr.substring(0, valuelen)
                                                                                                .compareToIgnoreCase(pathptr.substring(
                                                                                                        keylen + 1,
                                                                                                        keylen + 2))) {

                                        //   if (valuelen == pathvaluelen - (keylen + 1) && 0 == strncmp(valuestr, (pathptr + keylen + 1), valuelen))
                                        //       pathptr += valuelen + 2;
                                        //   else
                                        //       found = false;

                                        pathptr = new StringBuilder(pathptr.substring(valuelen + 2));
                                    } else {
                                        found = false;
                                    }
                                }

                                if (found) {
                                    // pathptr += keylen;
                                    pathptr = new StringBuilder(pathptr.substring(keylen));
                                    pathdepth = depth;

                                    if (pathptr.charAt(keylen) == '\0') {
                                        val = valuestr;
                                        len = valuelen;
                                        return val.substring(0, valuelen);
                                    }
                                }
                            }

                            depth = 0;
                            keylen = 0;
                            valuelen = 0;
                        }
                        state = newline;
                        break;
                    default:
                        if (state == space || state == newline) {
                            state = YamlState.key;
                            keystr = data;
                            keylen = 0; //redundant?
                        } else if (state == keysep) {
                            state = YamlState.value;
                            valuestr = data;
                            valuelen = 0; //redundant?
                        }
                        if (state == YamlState.key) {
                            keylen++;
                        }
                        if (state == YamlState.value) {
                            valuelen++;
                        }
                        break;
                }
            }
        }
        return "";
    }

}
