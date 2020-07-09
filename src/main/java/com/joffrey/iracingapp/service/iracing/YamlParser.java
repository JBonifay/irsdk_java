package com.joffrey.iracingapp.service.iracing;

import com.joffrey.iracingapp.model.defines.YamlState;
import org.springframework.stereotype.Component;

@Component
public class YamlParser {

    public boolean parseYaml(String data, String path, String val, int len) {

        if (!data.isEmpty() && path != null && !val.isEmpty() && len != 0) {
            // make sure we set this to something
            val = null;
            len = 0;

            int depth = 0;
            YamlState state = YamlState.space;

            String keystr = null;
            int keylen = 0;

            String valuestr = null;
            int valuelen = 0;

            String pathptr = path;
            int pathdepth = 0;

            while (!data.isEmpty()) {
                for (int i = 0; i < data.length(); i++) {
                    char c = data.charAt(i);
                    switch (c) {
                        case ' ':
                            if (state == YamlState.newline) {
                                state = YamlState.space;
                            }
                            if (state == YamlState.space) {
                                depth++;
                            } else if (state == YamlState.key) {
                                keylen++;
                            } else if (state == YamlState.value) {
                                valuelen++;
                            }
                            break;
                        case '-':
                            if (state == YamlState.newline) {
                                state = YamlState.space;
                            }
                            if (state == YamlState.space) {
                                depth++;
                            } else if (state == YamlState.key) {
                                keylen++;
                            } else if (state == YamlState.value) {
                                valuelen++;
                            } else if (state == YamlState.keysep) {
                                state = YamlState.value;
                                valuestr = data;
                                valuelen = 1;
                            }
                            break;
                        case ':':
                            if (state == YamlState.key) {
                                state = YamlState.keysep;
                                keylen++;
                            } else if (state == YamlState.keysep) {
                                state = YamlState.value;
                                valuestr = data;
                            } else if (state == YamlState.value) {
                                valuelen++;
                            }
                            break;
                        case '\n':
                        case '\r':
                            if (state != YamlState.newline) {
                                if (depth < pathdepth) {
                                    return false;
                                } else if (keylen != 0 && 0 == keystr.compareToIgnoreCase(path)) {
                                    boolean found = true;
                                    //do we need to test the value?
                                    if (pathptr.charAt(keylen) == '{') {
                                        //search for closing brace
                                        int pathvaluelen = keylen + 1;
                                        while (pathptr.charAt(pathvaluelen) != ' ' && (pathptr.charAt(pathvaluelen) != '}')) {
                                            pathvaluelen++;
                                            if (valuelen == pathvaluelen - (keylen + 1) && 0 == valuestr.compareToIgnoreCase(
                                                    String.valueOf(pathptr.charAt(keylen + 1)))) {

                                                pathptr += valuelen + 2;
                                            } else {
                                                found = false;
                                            }
                                        }
                                    }

                                    if (found) {
                                        pathptr += keylen;
                                        pathdepth = depth;

                                        if (pathptr.equals('\0')) {
                                            val = valuestr;
                                            len = valuelen;
                                            return true;
                                        }
                                    }
                                }

                                depth = 0;
                                keylen = 0;
                                valuelen = 0;
                            }
                            state = YamlState.newline;
                            break;
                        default:
                            if (state == YamlState.space || state == YamlState.newline) {
                                state = YamlState.key;
                                keystr = data;
                                keylen = 0; //redundant?
                            } else if (state == YamlState.keysep) {
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
        }
        return false;
    }


}
