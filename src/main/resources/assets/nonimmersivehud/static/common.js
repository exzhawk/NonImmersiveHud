const parseText = (line, defaultColor = 'gray') => {
    const parts = [];
    const ss = '§'
    let cursor = 0;
    let currentTextContent = '';
    let currentColor;
    let currentStyle;
    const reset = (resetAll) => {
        if (currentTextContent.length > 0) {
            // if (currentStyle.has('strikethrough')&&currentStyle.has('underline')){
            //     currentStyle.delete('strikethrough');
            //     currentStyle.delete('underline');
            //     currentStyle.add('strikethrough-underline');
            // }
            parts.push({text: currentTextContent, color: currentColor, style: Array.from(currentStyle)});
        }
        if (resetAll) {
            currentColor = defaultColor;
            currentStyle = new Set();
        } else {
            currentStyle = new Set(currentStyle);
        }

        currentTextContent = ''
    };
    reset(true);
    while (cursor < line.length) {
        let currentChar = line[cursor];
        if (currentChar === ss) {
            cursor++;
            if (!(cursor < line.length)) {
                break;
            }
            let code = line[cursor];
            switch (code) {
                case '0':
                    reset(true);
                    currentColor = 'black';
                    break;
                case '1':
                    reset(true);
                    currentColor = 'dark-blue';
                    break;
                case '2':
                    reset(true);
                    currentColor = 'dark-green';
                    break;
                case '3':
                    reset(true);
                    currentColor = 'dark-aqua';
                    break;
                case '4':
                    reset(true);
                    currentColor = 'dark-red';
                    break;
                case '5':
                    reset(true);
                    currentColor = 'dark-purple';
                    break;
                case '6':
                    reset(true);
                    currentColor = 'gold';
                    break;
                case '7':
                    reset(true);
                    currentColor = 'gray';
                    break;
                case '8':
                    reset(true);
                    currentColor = 'dark-gray';
                    break;
                case '9':
                    reset(true);
                    currentColor = 'blue';
                    break;
                case 'a':
                    reset(true);
                    currentColor = 'green';
                    break;
                case 'b':
                    reset(true);
                    currentColor = 'aqua';
                    break;
                case 'c':
                    reset(true);
                    currentColor = 'red';
                    break;
                case 'd':
                    reset(true);
                    currentColor = 'light-purple';
                    break;
                case 'e':
                    reset(true);
                    currentColor = 'yellow';
                    break;
                case 'f':
                    reset(true);
                    currentColor = 'white';
                    break;
                case 'k':
                    reset(false);
                    currentStyle.add('obfuscated');
                    break;
                case 'l':
                    reset(false);
                    currentStyle.add('bold');
                    break;
                case 'm':
                    reset(false);
                    currentStyle.add('strikethrough');
                    break;
                case 'n':
                    reset(false);
                    currentStyle.add('underline');
                    break
                case 'o':
                    reset(false);
                    currentStyle.add('italic');
                    break;
                case 'r':
                    reset(true);
                    break;
            }
        } else {
            currentTextContent += currentChar;
        }
        cursor++;
    }
    reset(true);
    return parts;
}
const keepScreenOnHack = () => {
// keep screen on hack from https://stackoverflow.com/a/46363553/2872958
    const video = document.createElement('video');
    video.setAttribute('loop', '');

    const addSourceToVideo = (element, type, dataURI) => {
        const source = document.createElement('source');
        source.src = dataURI;
        source.type = 'video/' + type;
        element.appendChild(source);
    }

    const base64 = (mimeType, base64) => {
        return 'data:' + mimeType + ';base64,' + base64;
    };
    addSourceToVideo(video, 'webm', base64('video/webm', 'GkXfo0AgQoaBAUL3gQFC8oEEQvOBCEKCQAR3ZWJtQoeBAkKFgQIYU4BnQI0VSalmQCgq17FAAw9CQE2AQAZ3aGFtbXlXQUAGd2hhbW15RIlACECPQAAAAAAAFlSua0AxrkAu14EBY8WBAZyBACK1nEADdW5khkAFVl9WUDglhohAA1ZQOIOBAeBABrCBCLqBCB9DtnVAIueBAKNAHIEAAIAwAQCdASoIAAgAAUAmJaQAA3AA/vz0AAA='));
    addSourceToVideo(video, 'mp4', base64('video/mp4', 'AAAAHGZ0eXBpc29tAAACAGlzb21pc28ybXA0MQAAAAhmcmVlAAAAG21kYXQAAAGzABAHAAABthADAowdbb9/AAAC6W1vb3YAAABsbXZoZAAAAAB8JbCAfCWwgAAAA+gAAAAAAAEAAAEAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAIVdHJhawAAAFx0a2hkAAAAD3wlsIB8JbCAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAQAAAAAAIAAAACAAAAAABsW1kaWEAAAAgbWRoZAAAAAB8JbCAfCWwgAAAA+gAAAAAVcQAAAAAAC1oZGxyAAAAAAAAAAB2aWRlAAAAAAAAAAAAAAAAVmlkZW9IYW5kbGVyAAAAAVxtaW5mAAAAFHZtaGQAAAABAAAAAAAAAAAAAAAkZGluZgAAABxkcmVmAAAAAAAAAAEAAAAMdXJsIAAAAAEAAAEcc3RibAAAALhzdHNkAAAAAAAAAAEAAACobXA0dgAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAIAAgASAAAAEgAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABj//wAAAFJlc2RzAAAAAANEAAEABDwgEQAAAAADDUAAAAAABS0AAAGwAQAAAbWJEwAAAQAAAAEgAMSNiB9FAEQBFGMAAAGyTGF2YzUyLjg3LjQGAQIAAAAYc3R0cwAAAAAAAAABAAAAAQAAAAAAAAAcc3RzYwAAAAAAAAABAAAAAQAAAAEAAAABAAAAFHN0c3oAAAAAAAAAEwAAAAEAAAAUc3RjbwAAAAAAAAABAAAALAAAAGB1ZHRhAAAAWG1ldGEAAAAAAAAAIWhkbHIAAAAAAAAAAG1kaXJhcHBsAAAAAAAAAAAAAAAAK2lsc3QAAAAjqXRvbwAAABtkYXRhAAAAAQAAAABMYXZmNTIuNzguMw=='));

    document.addEventListener('click', (e) => {
        if(e.target===document.body){
            return
        }
        // e.stopPropagation();
        if (document.fullscreenElement == null) {
            document.body.requestFullscreen();
            video.play();
        } else {
            document.exitFullscreen();
            video.pause();
        }
    }, false);
}
keepScreenOnHack()
const followOrientation = () => {
    screen.orientation.lock('landscape').catch((error) => {
        // whatever
    });
    screen.orientation.unlock();

}
followOrientation()
const autoReconnectWebsocket = (url, callback, timeout = 1000, interval = 60000) => {
    const connect = () => {
        const socket = new WebSocket(url);
        let heartbeat = null;
        socket.onopen = e => {
            console.log('connected');
            heartbeat = setInterval(() => socket.send('heartbeat'), 60000);
        };
        socket.onmessage = e => {
            console.log(e.data);
            callback(e.data)
        };
        socket.onclose = (e) => {
            console.log('connection closed');
            console.log(e);
            clearInterval(heartbeat);
            setTimeout(() => connect(), 1000);
        }
        socket.onerror = (e) => {
            console.log('connection error');
            console.log(e);
            socket.close();
        }
    }
    connect();
}
const resizeHudBox = (minScale = 0.2, maxScale = 1) => {
    let el = document.querySelector('#hud-box');
    let eW = el.offsetWidth;
    let eH = el.offsetHeight;
    let sW = window.innerWidth-16;
    let sH = window.innerHeight-16;
    let rawRatio = Math.min(sW / eW, sH / eH);
    let ratio = Math.max(Math.min(rawRatio, maxScale), minScale);
    el.style.transform = `scale(${ratio})`;
}
window.addEventListener("resize",()=>resizeHudBox())

const enableDarkModeSwitch=()=>{
    document.addEventListener("click",(e)=>{
        if (e.target===document.body){
            document.body.classList.toggle('dark');
        }
    })
}
enableDarkModeSwitch()
