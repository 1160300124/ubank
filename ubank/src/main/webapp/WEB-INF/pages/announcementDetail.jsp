<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1, maximum-scale=1, user-scalable=no">
            <meta http-equiv="X-UA-Compatible" content="ie=edge">
            <title>公告详情</title>
            <style>
                .title {
                    font-size: 15px;
                    font-weight: 700;
                    color: #333;
                    padding: 5px;
                }

                .time {
                    padding: 5px;
                    color: #999;
                }

                .content {
                    padding: 5px;
                    color: #666;
                }

                .files {
                    padding: 5px;
                    border-top: 2px solid #ededed;
                    width: 100%;
                }

                .file {
                    display: flex;
                    display: -webkit-flex;
                    display: -moz-flex;
                    height: 50px;
                    margin-top: 10px;
                }

                .file-icon {
                    width: 50px;
                }

                .file-icon>img {
                    width: 100%;
                    height: 100%;
                }

                .file-info {
                    flex: 1;
                    -webkit-flex: 1;
                    -moz-flex: 1;
                }

                .file-size {
                    color: #999;
                }

                table{
                    width: 100%;
                }
                #table_preview_attachment td:first-child{
                    width: 50px;
                }

                .pswp__button--download {
                    background-position: -1000px -1000px;
                    position: fixed !important;
                    right: 10px;
                    bottom: 10px;
                    z-index: 9999;
                }
            </style>
            <link href="<%=request.getContextPath()%>/css/photoswipe/photoswipe.css" rel="stylesheet" />
            <link href="<%=request.getContextPath()%>/css/photoswipe/default-skin/default-skin.css" rel="stylesheet" />
            <link href="<%=request.getContextPath()%>/css/font-awesome/font-awesome.min.css" rel="stylesheet" />
            <link href="<%=request.getContextPath()%>/css/bootstrap/bootstrap.min.css" rel="stylesheet" />

            <script src="<%=request.getContextPath()%>/js/jquery/zepto.min.js" type="text/javascript" ></script>
            <script src="<%=request.getContextPath()%>/js/photoswipe/photoswipe.min.js" type="text/javascript" ></script>
            <script src="<%=request.getContextPath()%>/js/photoswipe/photoswipe-ui-default.min.js" type="text/javascript" ></script>
        </head>

        <body>
            <div class="title">${title}</div>
            <div class="time">${companyName} &nbsp;&nbsp;&nbsp; ${createTime}</div>
            <div class="content"> ${content} </div>
            <div class="files">
                附件：
                <table id="table_preview_attachment">
                    <c:forEach items="${attachments}" var="attachment">
                        <tr data-down="${attachment.attachment_path}" data-preview="http://dcsapi.com/?k=262732077&url=${attachment.attachment_path}">
                            <td>
                                <div class="file-icon">
                                    <img src="<%=request.getContextPath()%>/images/pdf.png" alt="">
                                </div>
                            </td>
                            <td>
                                <div class="file-info">
                                    <div class="file-name">${attachment.attachment_name}</div>
                                    <div class="file-size">${attachment.attachment_size}</div>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>

            <!-- Root element of PhotoSwipe. Must have class pswp. -->
            <div class="pswp" tabindex="-1" role="dialog" aria-hidden="true">

                <!-- Background of PhotoSwipe. 
         It's a separate element as animating opacity is faster than rgba(). -->
                <div class="pswp__bg"></div>

                <!-- Slides wrapper with overflow:hidden. -->
                <div class="pswp__scroll-wrap">

                    <!-- Container that holds slides. 
            PhotoSwipe keeps only 3 of them in the DOM to save memory.
            Don't modify these 3 pswp__item elements, data is added later on. -->
                    <div class="pswp__container">
                        <div class="pswp__item"></div>
                        <div class="pswp__item"></div>
                        <div class="pswp__item"></div>
                    </div>

                    <!-- Default (PhotoSwipeUI_Default) interface on top of sliding area. Can be changed. -->
                    <div class="pswp__ui pswp__ui--hidden">

                        <div class="pswp__top-bar">

                            <!--  Controls are self-explanatory. Order can be changed. -->

                            <div class="pswp__counter"></div>

                            <!-- <button class="pswp__button pswp__button--close" title="Close (Esc)"></button> -->

                            <button class="pswp__button pswp__button--download" title="download">
                                <i class="glyphicon glyphicon-download-alt" style="color:#fff;font-size:16px;"></i>
                            </button>

                            <button class="pswp__button pswp__button--fs" title="Toggle fullscreen"></button>

                            <button class="pswp__button pswp__button--zoom" title="Zoom in/out"></button>

                            <!-- Preloader demo http://codepen.io/dimsemenov/pen/yyBWoR -->
                            <!-- element will get class pswp__preloader--active when preloader is running -->
                            <div class="pswp__preloader">
                                <div class="pswp__preloader__icn">
                                    <div class="pswp__preloader__cut">
                                        <div class="pswp__preloader__donut"></div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- <div class="pswp__share-modal pswp__share-modal--hidden pswp__single-tap">
                            <div class="pswp__share-tooltip"></div>
                        </div> -->

                        <button class="pswp__button pswp__button--arrow--left" title="Previous (arrow left)">
                        </button>

                        <button class="pswp__button pswp__button--arrow--right" title="Next (arrow right)">
                        </button>

                        <div class="pswp__caption">
                            <div class="pswp__caption__center"></div>
                        </div>

                    </div>

                </div>

            </div>

            <script type="text/javascript">
                var imgs = $('.content img');
                var gallery = null;

                var initPhotoSwipeFromDOM = function (gallerySelector) {

                    // parse slide data (url, title, size ...) from DOM elements 
                    // (children of gallerySelector)
                    var parseThumbnailElements = function (el) {
                        var items = [];
                        $(imgs).each(function (index, item) {
                            items.push({
                                src: item.src,
                                w: item.width,
                                h: item.height
                            })
                            $(item).attr('index', index);
                        })

                        return items;
                    };

                    // find nearest parent element
                    var closest = function closest(el, fn) {
                        return el && (fn(el) ? el : closest(el.parentNode, fn));
                    };

                    // triggers when user clicks on thumbnail
                    var onThumbnailsClick = function (e) {
                        e = e || window.event;
                        e.preventDefault ? e.preventDefault() : e.returnValue = false;

                        var eTarget = e.target || e.srcElement;

                        // find root element of slide
                        var clickedListItem = eTarget;

                        if (!clickedListItem) {
                            return;
                        }

                        // find index of clicked item by looping through all child nodes
                        // alternatively, you may define index via data- attribute
                        var clickedGallery = clickedListItem.parentNode,
                            childNodes = clickedListItem.parentNode.childNodes,
                            numChildNodes = childNodes.length,
                            nodeIndex = 0,
                            index;

                        for (var i = 0; i < numChildNodes; i++) {
                            if (childNodes[i].nodeType !== 1) {
                                continue;
                            }

                            if (childNodes[i] === clickedListItem) {
                                index = nodeIndex;
                                break;
                            }
                            nodeIndex++;
                        }



                        if (index >= 0) {
                            // open PhotoSwipe if valid index found
                            openPhotoSwipe(index, clickedGallery);
                        }
                        return false;
                    };

                    // parse picture index and gallery index from URL (#&pid=1&gid=2)
                    var photoswipeParseHash = function () {
                        var hash = window.location.hash.substring(1),
                            params = {};

                        if (hash.length < 5) {
                            return params;
                        }

                        var vars = hash.split('&');
                        for (var i = 0; i < vars.length; i++) {
                            if (!vars[i]) {
                                continue;
                            }
                            var pair = vars[i].split('=');
                            if (pair.length < 2) {
                                continue;
                            }
                            params[pair[0]] = pair[1];
                        }

                        if (params.gid) {
                            params.gid = parseInt(params.gid, 10);
                        }

                        return params;
                    };

                    var openPhotoSwipe = function (index, galleryElement, disableAnimation, fromURL) {
                        var pswpElement = $('.pswp')[0],
                            options,
                            items;

                        items = parseThumbnailElements(galleryElement);

                        // define options (if needed)
                        options = {

                            // define gallery index (for URL)
                            galleryUID: galleryElement.getAttribute('data-pswp-uid'),
                            closeOnVerticalDrag: false,
                            getThumbBoundsFn: function (index) {
                                // See Options -> getThumbBoundsFn section of documentation for more info
                                var thumbnail = galleryElement, // find thumbnail
                                    pageYScroll = window.pageYOffset || document.documentElement.scrollTop,
                                    rect = galleryElement.getBoundingClientRect();

                                return { x: rect.left, y: rect.top + pageYScroll, w: rect.width };
                            }

                        };

                        // PhotoSwipe opened from URL
                        if (fromURL) {
                            if (options.galleryPIDs) {
                                // parse real index when custom PIDs are used 
                                // http://photoswipe.com/documentation/faq.html#custom-pid-in-url
                                for (var j = 0; j < items.length; j++) {
                                    if (items[j].pid == index) {
                                        options.index = j;
                                        break;
                                    }
                                }
                            } else {
                                // in URL indexes start from 1
                                options.index = parseInt(index, 10) - 1;
                            }
                        } else {
                            options.index = parseInt(index, 10);
                        }

                        // exit if index not found
                        if (isNaN(options.index)) {
                            return;
                        }

                        if (disableAnimation) {
                            options.showAnimationDuration = 0;
                        }

                        // Pass data to PhotoSwipe and initialize it
                        gallery = new PhotoSwipe(pswpElement, PhotoSwipeUI_Default, items, options);
                        gallery.init();
                        window.app.openImgPreview();
                    };

                    // loop through all gallery elements and bind events
                    var galleryElements = document.querySelectorAll(gallerySelector);

                    for (var i = 0, l = galleryElements.length; i < l; i++) {
                        $(galleryElements[i]).attr('data-pswp-uid', i + 1);
                        galleryElements[i].onclick = onThumbnailsClick;
                    }

                    // Parse URL and open gallery if it contains #&pid=3&gid=1
                    var hashData = photoswipeParseHash();
                    if (hashData.pid && hashData.gid) {
                        openPhotoSwipe(hashData.pid, galleryElements[hashData.gid - 1], true, true);
                    }
                };

                // execute above function
                initPhotoSwipeFromDOM('.content img');

                //绑定下载图片按钮事件
                $('.pswp__button--download').on('touchstart', function(e) {
                    window.app.downloadImg(gallery.currItem.src);
                })

                var files = $('#table_preview_attachment tr');
                $(files).each(function(index, item) {
                    $(item).on('click', function(e) {
                        var target = e.currentTarget;
                        window.app.previewFile(target.getAttribute('data-preview'), target.getAttribute('data-down'), target.querySelector('.file-name').innerText, target.querySelector('.file-size').innerText);
                    })
                })

                /***
                 * 下载url @url
                 * */
                function downloadImg(url) {
                    
                }

                /***
                 * 预览url @previewUrl
                 * 下载url @downloadUrl
                 * */
                function previewFile(previewUrl, downloadUrl) {

                }

                function closeImgPreview() {
                    gallery.close();
                }
            </script>
        </body>

        </html>