/*
 * Copyright (c) 2004 Auster Solutions. All Rights Reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Created on Mar 16, 2005
 */
package br.com.auster.dware.console.request.views;

import java.io.Serializable;
import java.util.List;

/**
 * @author framos
 * @version $Id: FormatView.java 30 2005-05-24 18:46:06Z framos $
 */
public class FormatView implements Serializable {

    
    private String format;
    //private List trails;
    private List files;
    
    
    
    
    public final List getOutputFiles() {
        return files;
    }
    public final void setOutputFiles(List files) {
        this.files = files;
    }
    public final String getFormat() {
        return format;
    }
    public final void setFormat(String format) {
        this.format = format;
    }
//    public final List getTrails() {
//        return trails;
//    }
//    public final void setTrails(List trails) {
//        this.trails = trails;
//    }
    
}
